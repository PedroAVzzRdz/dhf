package com.example.dulcehorno.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dulcehorno.ReceiptManager;
import com.example.dulcehorno.UserSessionManager;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.utils.ErrorHandler;
import com.google.gson.Gson;
import com.example.dulcehorno.network.repository.UserRepository;
import com.example.dulcehorno.R;
import com.example.dulcehorno.model.UserProfileResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    private UserRepository userRepository;
    private Gson gson = new Gson();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar la vista y guardarla
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar repositorio
        userRepository = new UserRepository(requireContext());

        // Obtener perfil del usuario
        userRepository.getUserProfile(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                    );

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!isAdded()) return;

                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    UserProfileResponse userProfileResponse = gson.fromJson(responseBody, UserProfileResponse.class);
                    UserSessionManager.getInstance().setUserProfile(userProfileResponse);
                } else {
                    String errorMessage = ErrorHandler.getErrorMessage(responseBody);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    );

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // Obtener recibos del usuario
        userRepository.getUserReceipts(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                    );

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!isAdded()) return;

                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<Receipt>>() {}.getType();
                    List<Receipt> receipts = gson.fromJson(responseBody, listType);

                    for (Receipt r : receipts) {
                        ReceiptManager.getInstance().addReceipt(r);
                    }
                } else {
                    String errorMessage = ErrorHandler.getErrorMessage(responseBody);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    );

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        // Cargar fragment por defecto: Productos
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.homeFragmentContainer, new ProductsFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_products) {
                selectedFragment = new ProductsFragment();
            } else if (itemId == R.id.navigation_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.navigation_receipts) {
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeFragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
