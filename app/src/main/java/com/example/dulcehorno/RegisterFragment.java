package com.example.dulcehorno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textCuenta = view.findViewById(R.id.buttonGoLogin);
        textCuenta.setOnClickListener(v -> {
            // Reemplaza con LoginFragment
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
