package com.example.dulcehorno;

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

import com.example.dulcehorno.adapters.ProductAdapter;
import com.example.dulcehorno.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        // Inicializar listas
        allProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();

        // --- Rellena aquí tus productos (usa el constructor que tengas en Product) ---
        // Asegúrate de usar el orden correcto de parámetros según tu Product.java
        allProducts.add(new Product("p1", "Pan de Chocolate", 25.0, R.drawable.pan_chocolate, "Pan relleno de chocolate suave y recién horneado.", "Panadería"));
        allProducts.add(new Product("p2", "Croissant", 18.0, R.drawable.croissant, "Crujiente, mantecoso y delicioso para el desayuno.", "Panadería"));
        allProducts.add(new Product("p3", "Galleta", 10.0, R.drawable.galleta, "Galleta casera con chispas de chocolate.", "Repostería"));
        // agrega más productos si quieres...

        // Inicialmente mostrar todos
        filteredProducts.addAll(allProducts);

        // Adapter con ambos listeners (abrir detalle / agregar)
        adapter = new ProductAdapter(
                filteredProducts,
                this::openProductDetail,   // click en el producto → ver detalle
                this::showQuantityDialog   // click en “Agregar” → pedir cantidad
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        setupCategorySpinner();
        setupSearchInput();
    }

    private void setupCategorySpinner() {
        List<String> categories = collectCategories();
        // Crear ArrayAdapter para el spinner
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
     * Filtra productos por categoría seleccionada y texto de búsqueda (nombre).
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
     */
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
