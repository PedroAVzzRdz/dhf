package com.example.dulcehorno.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.R;
import com.example.dulcehorno.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> products;
    private final OnRemoveListener listener;

    public interface OnRemoveListener {
        void onRemove(Product product);
    }

    public CartAdapter(List<Product> products, OnRemoveListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textName.setText(product.getName());
        holder.textPrice.setText("$" + product.getPrice());
        holder.imageProduct.setImageResource(product.getDrawableResId());

        holder.buttonAdd.setText("Eliminar");
        holder.buttonAdd.setOnClickListener(v -> listener.onRemove(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice;
        Button buttonAdd;
        ImageView imageProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textProductName);
            textPrice = itemView.findViewById(R.id.textProductPrice);
            buttonAdd = itemView.findViewById(R.id.buttonAddToCart);
            imageProduct = itemView.findViewById(R.id.imageProduct);
        }
    }
}

