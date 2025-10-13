package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.adapters.ReceiptAdapter;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.model.UserProfileResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private TextView textUsername, textEmail;
    private ImageView imageProfile;
    private RecyclerView recyclerReceipts;

    private List<Receipt> receipts;
    private ReceiptAdapter adapter;
    private final Gson gson = new Gson();

    public HistoryFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textUsername = view.findViewById(R.id.textUsername);
        textEmail = view.findViewById(R.id.textEmail);
        imageProfile = view.findViewById(R.id.imageProfile);
        recyclerReceipts = view.findViewById(R.id.recyclerReceipts);

        receipts = new ArrayList<>();

        // Adapter con listener para abrir el detalle
        adapter = new ReceiptAdapter(receipts, position -> {
            Receipt selected = receipts.get(position);
            String receiptJson = gson.toJson(selected);
            ReceiptDetailDialogFragment dialog = ReceiptDetailDialogFragment.newInstance(receiptJson);
            dialog.show(requireActivity().getSupportFragmentManager(), "receipt_detail");
        });

        recyclerReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReceipts.setAdapter(adapter);

        // Cargar datos del singleton
        loadUserProfileFromSession();
        loadReceipts();
    }

    private void loadUserProfileFromSession() {
        UserProfileResponse profile = UserSession.getInstance().getUserProfile();
        if (profile != null) {
            textUsername.setText(profile.getUsername());
            textEmail.setText(profile.getEmail());
            imageProfile.setImageResource(R.drawable.ic_receipts);
        }
    }

    private void loadReceipts() {
        receipts.clear();
        receipts.addAll(ReceiptManager.getInstance().getReceipts());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReceipts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerReceipts.setAdapter(null);
    }
}
