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
import com.example.dulcehorno.model.CartItem;
import com.example.dulcehorno.model.Receipt;

import java.util.ArrayList;
import java.util.List;

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

            // Copia de los items del carrito para el recibo
            List<CartItem> itemsForReceipt = new ArrayList<>(CartManager.getInstance().getCartItems());

            // Crear id y fecha
            String id = String.valueOf(System.currentTimeMillis());
            String date = DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

            double total = CartManager.getInstance().getTotalPrice();

            // Crear y guardar recibo
            Receipt receipt = new Receipt(id, date, itemsForReceipt, total);
            ReceiptManager.getInstance().addReceipt(receipt);

            // Limpiar carrito
            CartManager.getInstance().clearCart();

            Toast.makeText(getContext(), "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
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
