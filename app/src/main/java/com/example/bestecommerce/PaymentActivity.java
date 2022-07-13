package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bestecommerce.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
 ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String ordercode = getIntent().getStringExtra("code");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.webview.setMixedContentAllowed(false);
        binding.webview.loadUrl(Constants.PAYMENT_URL+ "SR11656CD");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}