package com.example.dulcehorno.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dulcehorno.R;
import com.example.dulcehorno.UserSessionManager;
import com.example.dulcehorno.model.CartItem;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.model.UserProfileResponse;
import com.google.gson.Gson;

public class ReceiptDetailDialogFragment extends DialogFragment {

    private static final String ARG_RECEIPT_JSON = "arg_receipt_json";
    private final Gson gson = new Gson();
    private Receipt receipt;

    public static ReceiptDetailDialogFragment newInstance(String receiptJson) {
        ReceiptDetailDialogFragment f = new ReceiptDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIPT_JSON, receiptJson);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Aquí aplicamos el tema transparente
        setStyle(STYLE_NO_TITLE, R.style.Theme_TransparentDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% ancho
            getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_detail, container, false);

        String receiptJson = getArguments() != null ? getArguments().getString(ARG_RECEIPT_JSON) : null;
        if (receiptJson != null) {
            try {
                receipt = gson.fromJson(receiptJson, Receipt.class);
            } catch (Exception e) {
                receipt = null;
            }
        }

        TextView tvUserName = root.findViewById(R.id.tvDetailUserName);
        TextView tvUserEmail = root.findViewById(R.id.tvDetailUserEmail);
        TextView tvRequestDate = root.findViewById(R.id.tvDetailRequestDate);
        TextView tvEstimated = root.findViewById(R.id.tvDetailEstimated);
        TextView tvLocation = root.findViewById(R.id.tvDetailLocation);
        TextView tvItems = root.findViewById(R.id.tvDetailItems);
        TextView tvTotal = root.findViewById(R.id.tvDetailTotal);
        ImageView btnClose = root.findViewById(R.id.btnCloseDialog);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dismiss());
        }


        UserProfileResponse user = UserSessionManager.getInstance().getUserProfile();
        if (user != null) {
            if (tvUserName != null) tvUserName.setText(user.getUsername());
            if (tvUserEmail != null) tvUserEmail.setText(user.getEmail());
        } else {
            if (tvUserName != null) tvUserName.setVisibility(View.GONE);
            if (tvUserEmail != null) tvUserEmail.setVisibility(View.GONE);
        }

        if (receipt != null) {
            if (tvRequestDate != null) tvRequestDate.setText("Pedido: " + safe(receipt.getRequestDate()));
            if (tvEstimated != null) tvEstimated.setText("Llegará aprox.: " + safe(receipt.getEstimatedArrivalDate()));
            if (tvLocation != null) tvLocation.setText("Dirección: " + safe(receipt.getDeliveryLocation()));

            StringBuilder sb = new StringBuilder();
            if (receipt.getItems() != null) {
                for (CartItem item : receipt.getItems()) {
                    if (item == null || item.getProduct() == null) continue;
                    double sub = item.getQuantity() * item.getProduct().getPrice();
                    sb.append(String.format("%s x%d — $%.2f\n",
                            safe(item.getProduct().getName()),
                            item.getQuantity(),
                            sub));
                }
            }
            if (tvItems != null) tvItems.setText(sb.toString().trim());

            if (tvTotal != null) tvTotal.setText(String.format("Total: $%.2f", receipt.getTotal()));
        } else {
            if (tvRequestDate != null) tvRequestDate.setText("Pedido: -");
            if (tvEstimated != null) tvEstimated.setText("Llegará aprox.: -");
            if (tvLocation != null) tvLocation.setText("Dirección: -");
            if (tvItems != null) tvItems.setText("-");
            if (tvTotal != null) tvTotal.setText("Total: $0.00");
        }

        return root;
    }

    private String safe(String s) {
        return s == null ? "-" : s;
    }
}
