package com.example.dulcehorno;

import android.os.Bundle;
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
        // Aquí puedes agregar productos de ejemplo
        products.add(new Product("Pan de Chocolate", 25.0, R.drawable.pan_chocolate));
        products.add(new Product("Croissant", 18.0, R.drawable.croissant));
        products.add(new Product("Galleta", 10.0, R.drawable.galleta));

        adapter = new ProductAdapter(products, product -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(getContext(), product.getName() + " agregado al carrito", Toast.LENGTH_SHORT).show();
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Aquí puedes agregar lógica para buscar o filtrar usando editTextSearch y spinnerCategory
    }
}
