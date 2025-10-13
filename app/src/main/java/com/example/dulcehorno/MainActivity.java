package com.example.dulcehorno;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dulcehorno.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Carga inicial: WelcomeFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();
    }
}
