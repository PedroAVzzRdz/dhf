package com.example.dulcehorno.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.R;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.model.CartItem;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private final List<Receipt> receipts;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ReceiptAdapter(List<Receipt> receipts, OnItemClickListener listener) {
        this.receipts = receipts;
        this.listener = listener;
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

        // Solo mostrar fecha y total
        holder.textRequestDate.setText("Pedido: " + receipt.getRequestDate());
        holder.textTotal.setText(String.format("Total: $%.2f", receipt.getTotal()));

        // Click para mostrar detalle
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(position);
        });
    }


    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView textRequestDate, textTotal;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            textRequestDate = itemView.findViewById(R.id.textReceiptRequestDate);
            textTotal = itemView.findViewById(R.id.textReceiptTotal);
        }
    }

}
