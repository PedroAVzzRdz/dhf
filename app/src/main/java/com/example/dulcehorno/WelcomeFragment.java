package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout de este fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Botón Iniciar Sesión
        Button btnLogin = view.findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Texto "cuenta" en rojo para ir a registro
        TextView textCuenta = view.findViewById(R.id.textCuenta);
        textCuenta.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
