package com.example.dulcehorno.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dulcehorno.R;
import com.example.dulcehorno.model.Product;

public class ProductDetailFragment extends DialogFragment {

    private static final String ARG_PRODUCT = "arg_product";

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment f = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        f.setArguments(args);
        return f;
    }

    private Product product;
    private ImageView image, btnClose;
    private TextView tvName, tvCategory, tvPrice, tvDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // permitir cancelar con BACK
        setCancelable(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear dialog y asegurarnos de que sea cancelable al tocar fuera
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Configurar ventana: ancho, transparencia y flags para que sea modal real
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();

            // 90% ancho pantalla
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

            // fondo de la ventana transparente (tu layout ya tiene overlay)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Asegurar que la ventana BLOQUEE los toques al fondo (no deje pasar events)
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            // Añadir dimming detrás
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.45f);

            // evitar que el contenido debajo reciba focus/click
            // (por si acaso) también removemos FLAG_NOT_FOCUSABLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

        product = (Product) (getArguments() != null ? getArguments().getSerializable(ARG_PRODUCT) : null);

        image = root.findViewById(R.id.imageProductDetail);
        tvName = root.findViewById(R.id.textProductNameDetail);
        tvCategory = root.findViewById(R.id.textProductCategory);
        tvPrice = root.findViewById(R.id.textProductPriceDetail);
        tvDescription = root.findViewById(R.id.textProductDescription);
        btnClose = root.findViewById(R.id.btnCloseDialog);

        // Cerrar al tocar el botón
        btnClose.setOnClickListener(v -> dismiss());

        // Rellenar datos
        if (product != null) {
            int resId = requireContext().getResources().getIdentifier(
                    product.getDrawableResId(), "drawable", requireContext().getPackageName()
            );

            image.setImageResource(resId);
            tvName.setText(product.getName());
            tvCategory.setText(product.getCategory());
            tvPrice.setText(String.format("$%.2f", product.getPrice()));
            tvDescription.setText(product.getDescription());
        }

        // Aseguramos que el root consume clicks (por seguridad)
        root.setClickable(true);
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);

        return root;
    }
}
