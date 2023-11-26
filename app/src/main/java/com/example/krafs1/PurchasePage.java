package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class PurchasePage extends AppCompatActivity {
    private RecyclerView rvPurchase;
    private MaterialButton payNowBtn, back_btn;
    private String userId;
    private String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);

        //Back Button
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(PurchasePage.this, CartPage.class);
                startActivity(backIntent);
            }
        });

        // Receive product data from CartPage
        Intent intent = getIntent();
        List<CartPage.Cart> cartList = intent.getParcelableArrayListExtra("cartList");

        // RecyclerView
        rvPurchase = findViewById(R.id.rvPurchase);
        // Change this line in your PurchasePage activity
        rvPurchase.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        PurchaseAdapter purchaseAdapter = new PurchaseAdapter(cartList);
        rvPurchase.setAdapter(purchaseAdapter);


    }


}
