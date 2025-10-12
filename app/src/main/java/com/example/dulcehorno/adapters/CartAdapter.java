package com.example.dulcehorno.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.R;
import com.example.dulcehorno.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> cartItems;
    private final OnRemoveClickListener listener;

    public interface OnRemoveClickListener {
        void onRemoveClick(CartItem cartItem);
    }

    public CartAdapter(List<CartItem> cartItems, OnRemoveClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.textName.setText(item.getProduct().getName());
        holder.textPrice.setText(String.format("Precio: $%.2f", item.getProduct().getPrice()));
        holder.textQuantity.setText(String.format("Cantidad: %d", item.getQuantity()));

        double subtotal = item.getProduct().getPrice() * item.getQuantity();
        holder.textSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));

        holder.buttonRemove.setOnClickListener(v -> listener.onRemoveClick(item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity, textSubtotal;
        Button buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textCartName);
            textPrice = itemView.findViewById(R.id.textCartPrice);
            textQuantity = itemView.findViewById(R.id.textCartQuantity);
            textSubtotal = itemView.findViewById(R.id.textCartSubtotal);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
