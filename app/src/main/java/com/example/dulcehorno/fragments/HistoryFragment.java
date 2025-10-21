package com.example.dulcehorno.fragments;

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

import com.example.dulcehorno.R;
import com.example.dulcehorno.ReceiptManager;
import com.example.dulcehorno.UserSessionManager;
import com.example.dulcehorno.adapters.ReceiptAdapter;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.model.UserProfileResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private TextView textUsername, textEmail;
    private ImageView imageProfile; // Mantengo el ImageView para cargar el perfil si es necesario
    private RecyclerView recyclerReceipts;

    private List<Receipt> receipts;
    private ReceiptAdapter adapter;
    private final Gson gson = new Gson();

    public HistoryFragment() {
        // Constructor público requerido por Android
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout modificado (fragment_history.xml)
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a las vistas del perfil y la lista (los IDs no cambiaron)
        textUsername = view.findViewById(R.id.textUsername);
        textEmail = view.findViewById(R.id.textEmail);
        imageProfile = view.findViewById(R.id.imageProfile);
        recyclerReceipts = view.findViewById(R.id.recyclerReceipts);

        receipts = new ArrayList<>();

        // Adapter con listener para abrir el detalle (Funcionalidad original mantenida)
        adapter = new ReceiptAdapter(receipts, position -> {
            Receipt selected = receipts.get(position);
            String receiptJson = gson.toJson(selected);
            // Asumo que ReceiptDetailDialogFragment maneja el JSON para mostrar los detalles
            ReceiptDetailDialogFragment dialog = ReceiptDetailDialogFragment.newInstance(receiptJson);
            dialog.show(requireActivity().getSupportFragmentManager(), "receipt_detail");
        });

        recyclerReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReceipts.setAdapter(adapter);

        // Cargar datos del singleton
        loadUserProfileFromSession();
        loadReceipts();
    }

    /**
     * Carga el nombre y el email del usuario en las tarjetas de perfil.
     */
    private void loadUserProfileFromSession() {
        UserProfileResponse profile = UserSessionManager.getInstance().getUserProfile();
        if (profile != null) {
            textUsername.setText(profile.getUsername());
            textEmail.setText(profile.getEmail());
            // Se mantiene la imagen estática del recibo en el XML, pero si hubiera una
            // imagen de perfil real, se cargaría aquí.
            imageProfile.setImageResource(R.drawable.ic_receipts);
        }
    }

    /**
     * Carga la lista de recibos desde el gestor y actualiza el RecyclerView.
     */
    private void loadReceipts() {
        // Mantenemos la lógica de historial
        receipts.clear();
        // Asumo que ReceiptManager.getInstance().getReceipts() es la lista de datos.
        receipts.addAll(ReceiptManager.getInstance().getReceipts());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar la lista de recibos cada vez que el fragmento se hace visible.
        loadReceipts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Buena práctica para evitar fugas de memoria con RecyclerView.
        recyclerReceipts.setAdapter(null);
    }
}