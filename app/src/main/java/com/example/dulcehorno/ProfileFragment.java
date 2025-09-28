package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dulcehorno.model.UserProfile;
import com.example.dulcehorno.network.repository.UserRepository;
import com.example.dulcehorno.utils.ErrorHandler;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private TextView textProfile;
    private UserRepository userRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textProfile = view.findViewById(R.id.textProfile);
        userRepository = new UserRepository(requireContext());

        // Llamada automática al cargar la UI
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
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    UserProfile profile = new Gson().fromJson(responseBody, UserProfile.class);
                    requireActivity().runOnUiThread(() ->
                            textProfile.setText("Bienvenido, " + profile.getUsername())
                    );
                } else if (response.code() == 401) {
                    // Token expirado o inválido → redirigir al login
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Sesión expirada. Por favor, inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();

                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, new LoginFragment())
                                .commit();
                    });
                } else {
                    // Mostrar mensaje de error usando ErrorHandler
                    String errorMessage = ErrorHandler.getErrorMessage(responseBody);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            }

        });

    }
}
