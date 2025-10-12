package com.example.dulcehorno;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dulcehorno.adapters.CartAdapter;
import com.example.dulcehorno.model.Product;
import com.example.dulcehorno.model.Receipt;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerCart;
    private TextView textTotal;
    private Button buttonPay;

    private CartAdapter adapter;
    private List<Product> cartItems;

    // Guardamos la referencia al listener para poder removerla en onDestroyView
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

        // Usamos directamente la lista del CartManager para que los cambios se reflejen
        cartItems = CartManager.getInstance().getCartItems();

        adapter = new CartAdapter(cartItems, product -> {
            // Cuando el usuario pulsa "Eliminar" en un item
            CartManager.getInstance().removeFromCart(product);
            // No es necesario llamar adapter.notifyDataSetChanged() aquí porque
            // removeFromCart() notifica a los listeners y nuestro listener actualiza la UI.
        });

        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(adapter);

        // Registrar listener para actualizaciones del carrito
        CartManager.getInstance().addListener(cartListener);

        // Inicializar total
        updateTotal();

        buttonPay.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear copia de items para el recibo
            List<Product> itemsForReceipt = new ArrayList<>(CartManager.getInstance().getCartItems());

            // Crear id y fecha
            String id = String.valueOf(System.currentTimeMillis());
            String date = DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

            double total = CartManager.getInstance().getTotalPrice();

            // Construir recibo y guardarlo en ReceiptManager
            Receipt receipt = new Receipt(id, date, itemsForReceipt, total);
            ReceiptManager.getInstance().addReceipt(receipt);

            // Limpiar carrito (esto disparará el listener y actualizará la UI)
            CartManager.getInstance().clearCart();

            Toast.makeText(getContext(), "Compra realizada con éxito", Toast.LENGTH_SHORT).show();

            // Opcional: navegar a pestaña Historial si quieres (depende de tu nav setup)
            // Ejemplo (si tienes acceso al BottomNavigationView): seleccionar el item de historial.
        });
    }

    private void onCartUpdated() {
        // Se ejecuta cuando CartManager notifica cambios.
        requireActivity().runOnUiThread(() -> {
            // adapter usa la misma lista de CartManager, así que basta notificar el cambio
            adapter.notifyDataSetChanged();
            updateTotal();
        });
    }

    private void updateTotal() {
        double total = CartManager.getInstance().getTotalPrice();
        // Formatear con dos decimales
        String totalText = String.format("Total: $%.2f", total);
        textTotal.setText(totalText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Quitar el listener para evitar leaks
        CartManager.getInstance().removeListener(cartListener);
    }
}
