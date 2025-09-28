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
import com.example.dulcehorno.network.request.SignupRequest;
import com.example.dulcehorno.utils.ErrorHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterFragment extends Fragment {

    private EditText editTextEmail, editTextUsername, editTextPassword;
    private Button buttonSignup;
    private AuthRepository authRepository;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSignup = view.findViewById(R.id.buttonSignup);

        authRepository = new AuthRepository(requireContext());

        buttonSignup.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            SignupRequest request = new SignupRequest(email, username, password);

            authRepository.signup(request, new Callback() {
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
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

                            // Ir al LoginFragment
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, new LoginFragment())
                                    .addToBackStack(null)
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
        });

        // Botón para ir al login si quiere volver
        TextView textCuenta = view.findViewById(R.id.buttonGoLogin);
        textCuenta.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new LoginFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }
}
