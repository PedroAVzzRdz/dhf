package com.example.dulcehorno.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.R;
import com.example.dulcehorno.model.Receipt;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private final List<Receipt> receipts;

    public ReceiptAdapter(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);
        holder.textDate.setText("Fecha: " + receipt.getDate());
        holder.textTotal.setText("Total: $" + receipt.getTotal());
        holder.textProducts.setText("Productos: " + receipt.getItems().size());
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTotal, textProducts;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textReceiptDate);
            textTotal = itemView.findViewById(R.id.textReceiptTotal);
            textProducts = itemView.findViewById(R.id.textReceiptProducts);
        }
    }
}
