package com.example.dulcehorno;
// CartManager.java

import com.example.dulcehorno.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private final List<Product> cartItems;
    private final List<CartChangeListener> listeners = new ArrayList<>();

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public interface CartChangeListener {
        void onCartUpdated();
    }

    public void addListener(CartChangeListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(CartChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (CartChangeListener l : new ArrayList<>(listeners)) {
            l.onCartUpdated();
        }
    }

    public void addToCart(Product product) {
        cartItems.add(product);
        notifyListeners();
    }

    public void removeFromCart(Product product) {
        cartItems.remove(product);
        notifyListeners();
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        notifyListeners();
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : cartItems) total += p.getPrice();
        return total;
    }
}
