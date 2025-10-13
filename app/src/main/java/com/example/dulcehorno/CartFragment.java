package com.example.dulcehorno;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dulcehorno.adapters.CartAdapter;
import com.example.dulcehorno.model.CartItem;
import com.example.dulcehorno.model.Receipt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CartFragment extends Fragment {

    private RecyclerView recyclerCart;
    private TextView textTotal;
    private Button buttonPay;

    private CartAdapter adapter;
    private List<CartItem> cartItems;

    // Listener para actualizaciones del carrito
    private final CartManager.CartChangeListener cartListener = this::onCartUpdated;

    public CartFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerCart = view.findViewById(R.id.recyclerCart);
        textTotal = view.findViewById(R.id.textTotal);
        buttonPay = view.findViewById(R.id.buttonPay);

        // Obtener los items del carrito
        cartItems = CartManager.getInstance().getCartItems();

        adapter = new CartAdapter(cartItems, cartItem -> {
            // eliminar el producto (usa el product dentro del cartItem)
            CartManager.getInstance().removeFromCart(cartItem.getProduct());
        });

        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(adapter);

        // Registrar listener
        CartManager.getInstance().addListener(cartListener);

        // Mostrar total inicial
        updateTotal();

        buttonPay.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar diálogo para ingresar la dirección/ubicación de entrega
            final EditText input = new EditText(requireContext());
            input.setHint("Dirección o referencia de entrega");

            new AlertDialog.Builder(requireContext())
                    .setTitle("Ubicación de entrega")
                    .setMessage("Ingresa la dirección donde deseas recibir tu pedido:")
                    .setView(input)
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        String location = input.getText() != null ? input.getText().toString().trim() : "";
                        if (location.isEmpty()) {
                            Toast.makeText(getContext(), "Debes ingresar una ubicación válida", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Generar fechas:
                        // requestDate = ahora
                        String requestDate = DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

                        // estimatedArrival: sumar días aleatorios (por ejemplo 1..5 días)
                        Random rnd = new Random();
                        int extraDays = rnd.nextInt(5) + 1; // 1..5
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_MONTH, extraDays);
                        String estimatedArrival = DateFormat.format("dd/MM/yyyy", cal).toString();

                        // Copia de items (para guardar snapshot en recibo)
                        List<CartItem> itemsForReceipt = new ArrayList<>();
                        for (CartItem ci : CartManager.getInstance().getCartItems()) {
                            // create shallow copy - CartItem has product reference and quantity
                            itemsForReceipt.add(new CartItem(ci.getProduct(), ci.getQuantity()));
                        }

                        double total = CartManager.getInstance().getTotalPrice();
                        String id = String.valueOf(System.currentTimeMillis());

                        // Crear y guardar recibo
                        Receipt receipt = new Receipt(id, requestDate, estimatedArrival, location, itemsForReceipt, total);
                        ReceiptManager.getInstance().addReceipt(receipt);

                        // Limpiar carrito
                        CartManager.getInstance().clearCart();

                        Toast.makeText(getContext(), "Compra realizada. Entrega estimada: " + estimatedArrival, Toast.LENGTH_LONG).show();

                        // Opcional: navegar a Historial
                        // Si tienes un BottomNavigationView, selecciona el item de historial aquí (depende de tu estructura).
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void onCartUpdated() {
        requireActivity().runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            updateTotal();
        });
    }

    private void updateTotal() {
        double total = CartManager.getInstance().getTotalPrice();
        textTotal.setText(String.format("Total: $%.2f", total));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CartManager.getInstance().removeListener(cartListener);
    }
}
