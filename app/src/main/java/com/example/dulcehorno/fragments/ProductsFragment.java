package com.example.dulcehorno.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.CartManager;
import com.example.dulcehorno.ProductManager;
import com.example.dulcehorno.R;
import com.example.dulcehorno.ReceiptManager;
import com.example.dulcehorno.adapters.ProductAdapter;
import com.example.dulcehorno.model.Product;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.network.repository.ProductsRepository;
import com.example.dulcehorno.utils.ErrorHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private Spinner spinnerCategory;
    private ProductAdapter adapter;

    // allProducts = fuente de verdad; filteredProducts = lo que muestra el adapter
    private List<Product> allProducts;
    private List<Product> filteredProducts;

    public ProductsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        filteredProducts.clear();
        filteredProducts.addAll(ProductManager.getInstance().getProducts());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        allProducts = ProductManager.getInstance().getProducts();
        filteredProducts = new ArrayList<>(allProducts);

        adapter = new ProductAdapter(
                filteredProducts,
                this::openProductDetail,
                this::showQuantityDialog
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        setupCategorySpinner();
        setupSearchInput();
    }


    private void setupCategorySpinner() {
        List<String> categories = collectCategories();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { /* no-op */ }
        });
    }

    private List<String> collectCategories() {
        Set<String> set = new HashSet<>();
        for (Product p : allProducts) {
            if (p.getCategory() != null && !p.getCategory().trim().isEmpty()) {
                set.add(p.getCategory().trim());
            }
        }
        List<String> list = new ArrayList<>();
        list.add("Todas"); // opción por defecto
        list.addAll(set);
        return list;
    }

    private void setupSearchInput() {
        // TextWatcher para búsquedas en tiempo real
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    /**
//     * Filtra productos por categoría seleccionada y texto de búsqueda (nombre).
     * Actualiza filteredProducts y notifica al adapter.
     */
    private void applyFilters() {
        String selectedCategory = (String) spinnerCategory.getSelectedItem();
        String query = editTextSearch.getText() != null ? editTextSearch.getText().toString().trim().toLowerCase(Locale.ROOT) : "";

        filteredProducts.clear();
        for (Product p : allProducts) {
            boolean matchesCategory = "Todas".equals(selectedCategory) || selectedCategory == null
                    || (p.getCategory() != null && p.getCategory().equals(selectedCategory));
            boolean matchesQuery = query.isEmpty() || (p.getName() != null && p.getName().toLowerCase(Locale.ROOT).contains(query));

            if (matchesCategory && matchesQuery) {
                filteredProducts.add(p);
            }
        }

        // Notificar cambios
        adapter.notifyDataSetChanged();
    }

    /**
     * Muestra un diálogo para elegir la cantidad y agrega el producto al carrito.
     * Antes verifica que no se exceda el límite de unidades disponibles.
     */
    private void showQuantityDialog(Product product) {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Cantidad (ej. 1)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Cantidad")
                .setMessage("¿Cuántas unidades quieres agregar?\nDisponibles: " + product.getAvailableUnits())
                .setView(input)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    int qty;

                    try {
                        qty = Integer.parseInt(text);
                        if (qty <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Cantidad inválida, se usará 1.", Toast.LENGTH_SHORT).show();
                        qty = 1;
                    }

                    // 🔍 Verificación de stock disponible
                    if (product.getAvailableUnits() <= 0) {
                        Toast.makeText(getContext(), "Este producto está agotado.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (qty > product.getAvailableUnits()) {
                        Toast.makeText(getContext(), "Solo hay " + product.getAvailableUnits() + " unidades disponibles.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // ✅ Si la cantidad es válida, agregar al carrito
                    CartManager.getInstance().addToCart(product, qty);
                    Toast.makeText(getContext(), product.getName() + " x" + qty + " agregado al carrito", Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    /**
     * Abre un fragmento con más información del producto.
     */
    private void openProductDetail(Product product) {
        ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(product);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeFragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
