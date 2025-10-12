package com.example.dulcehorno;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.adapters.ProductAdapter;
import com.example.dulcehorno.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private Spinner spinnerCategory;
    private ProductAdapter adapter;
    private List<Product> products;

    public ProductsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        products = new ArrayList<>();
        products.add(new Product("p1", "Pan de Chocolate", 25.0, R.drawable.pan_chocolate));
        products.add(new Product("p2", "Croissant", 18.0, R.drawable.croissant));
        products.add(new Product("p3", "Galleta", 10.0, R.drawable.galleta));

        adapter = new ProductAdapter(products, product -> {
            // Al pulsar agregar: pedir cantidad
            showQuantityDialog(product);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showQuantityDialog(Product product) {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Cantidad (ej. 1)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Cantidad")
                .setMessage("¿Cuántas unidades quieres agregar?")
                .setView(input)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    int qty = 1;
                    try {
                        qty = Integer.parseInt(text);
                        if (qty <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Cantidad inválida, se usará 1.", Toast.LENGTH_SHORT).show();
                        qty = 1;
                    }
                    CartManager.getInstance().addToCart(product, qty);
                    Toast.makeText(getContext(), product.getName() + " x" + qty + " agregado al carrito", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
