package com.example.dulcehorno;

import com.example.dulcehorno.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    private final List<Product> products = new ArrayList<>();

    private ProductManager() {}

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> newProducts) {
        products.clear();
        if (newProducts != null) {
            products.addAll(newProducts);
        }
    }

    public void addProduct(Product product) {
        if (product != null) products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void clear() {
        products.clear();
    }

    public void decreaseProductUnits(String productId, int quantity) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                Product updated = new Product(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getDrawableResId(),
                        p.getDescription(),
                        p.getCategory(),
                        Math.max(0, p.getAvailableUnits() - quantity)
                );
                int index = products.indexOf(p);
                products.set(index, updated);
                break;
            }
        }
    }
}
