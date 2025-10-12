package com.example.dulcehorno;
// CartManager.java

import com.example.dulcehorno.model.CartItem;
import com.example.dulcehorno.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();
    private final List<CartChangeListener> listeners = new ArrayList<>();

    public interface CartChangeListener { void onCartUpdated(); }

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addListener(CartChangeListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    public void removeListener(CartChangeListener l) {
        listeners.remove(l);
    }

    private void notifyListeners() {
        for (CartChangeListener l : new ArrayList<>(listeners)) l.onCartUpdated();
    }

    // Añadir producto con cantidad — si ya existe, sumar la cantidad
    public void addToCart(Product product, int quantity) {
        if (quantity <= 0) return;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                notifyListeners();
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
        notifyListeners();
    }

    public void removeFromCart(Product product) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(product.getId())) {
                cartItems.remove(i);
                notifyListeners();
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        notifyListeners();
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) total += item.getLineTotal();
        return total;
    }
}