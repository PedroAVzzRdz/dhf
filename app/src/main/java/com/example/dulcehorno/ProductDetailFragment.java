package com.example.dulcehorno;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dulcehorno.model.Product;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT = "arg_product";

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment f = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        f.setArguments(args);
        return f;
    }

    private Product product;
    private ImageView image;
    private TextView tvName, tvCategory, tvPrice, tvDescription;
    private EditText editQty;
    private Button btnAdd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        product = (Product) (getArguments() != null ? getArguments().getSerializable(ARG_PRODUCT) : null);
        image = view.findViewById(R.id.imageProductDetail);
        tvName = view.findViewById(R.id.textProductNameDetail);
        tvCategory = view.findViewById(R.id.textProductCategory);
        tvPrice = view.findViewById(R.id.textProductPriceDetail);
        tvDescription = view.findViewById(R.id.textProductDescription);

        if (product != null) {
            image.setImageResource(product.getDrawableResId());
            tvName.setText(product.getName());
            tvCategory.setText(product.getCategory());
            tvPrice.setText(String.format("$%.2f", product.getPrice()));
            tvDescription.setText(product.getDescription());
        }

    }
}
