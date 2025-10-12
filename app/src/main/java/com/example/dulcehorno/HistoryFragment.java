package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulcehorno.adapters.ReceiptAdapter;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.model.UserProfileResponse;
import com.example.dulcehorno.network.repository.UserRepository;
import com.example.dulcehorno.utils.ErrorHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private TextView textUsername, textEmail;
    private ImageView imageProfile;
    private RecyclerView recyclerReceipts;

    private List<Receipt> receipts;
    private ReceiptAdapter adapter;
    private UserRepository userRepository;

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

        userRepository = new UserRepository(requireContext());
        receipts = new ArrayList<>();
        adapter = new ReceiptAdapter(receipts);

        recyclerReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReceipts.setAdapter(adapter);

        loadUserProfile();
        loadReceipts();
    }

    private void loadUserProfile() {
        userRepository.getUserProfile(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Leer el body UNA sola vez
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    UserProfileResponse profile = new Gson().fromJson(responseBody, UserProfileResponse.class);
                    requireActivity().runOnUiThread(() -> {
                        textUsername.setText(profile.getUsername());
                        textEmail.setText(profile.getEmail());
                        imageProfile.setImageResource(R.drawable.ic_receipts);
                    });

                } else if (response.code() == 401 || response.code() == 403) {
                    // Token inválido o sesión expirada → redirigir al login
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(
                                getContext(),
                                "Sesión expirada. Por favor, inicia sesión nuevamente.",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Redirigir al fragmento de login
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, new LoginFragment())
                                .commit();
                    });

                } else {
                    // Otros errores → manejar con ErrorHandler
                    String errorMessage = ErrorHandler.getErrorMessage(responseBody);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            }

        });
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
        // evitar leaks: cortar referencia al adapter/recycler (opcional)
        recyclerReceipts.setAdapter(null);
    }
}
