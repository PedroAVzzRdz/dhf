// package com.example.dulcehorno.adapters;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public interface OnAddClickListener {
        void onAddClick(Product product);
    }

    private final List<Product> productList;
    private final OnItemClickListener itemClickListener;
    private final OnAddClickListener addClickListener;

    public ProductAdapter(List<Product> productList, OnItemClickListener itemClickListener, OnAddClickListener addClickListener) {
        this.productList = productList;
        this.itemClickListener = itemClickListener;
        this.addClickListener = addClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.textName.setText(p.getName());
        holder.textPrice.setText(String.format("$%.2f", p.getPrice()));
        holder.imageProduct.setImageResource(p.getDrawableResId());

        // click en todo el item → abrir detalle
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onItemClick(p);
        });

        // click en el botón agregar → pedir cantidad (o delegar)
        holder.buttonAdd.setOnClickListener(v -> {
            if (addClickListener != null) addClickListener.onAddClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName, textPrice;
        Button buttonAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textName = itemView.findViewById(R.id.textProductName);
            textPrice = itemView.findViewById(R.id.textProductPrice);
            buttonAdd = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}

