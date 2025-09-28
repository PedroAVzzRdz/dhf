package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dulcehorno.network.repository.AuthRepository;
import com.example.dulcehorno.network.request.LoginRequest;
import com.example.dulcehorno.model.TokenResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private AuthRepository authRepository;

    public LoginFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        authRepository = new AuthRepository(requireContext());

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(username, password);

            authRepository.login(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        // 👇 Aquí asumo que el backend devuelve JSON con el token
                        // Ejemplo: { "token": "xxxx.yyyy.zzzz" }
                        String token = new Gson()
                                .fromJson(result, TokenResponse.class)
                                .getToken();

                        authRepository.saveToken(token); // Guardamos JWT ✅

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Login exitoso", Toast.LENGTH_SHORT).show();

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, new ProfileFragment())
                                    .addToBackStack(null) // opcional: permite volver con el botón atrás
                                    .commit();
                        });

                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });

        });

        TextView textCuenta = view.findViewById(R.id.buttonGoRegister);
        textCuenta.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new RegisterFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }
}
