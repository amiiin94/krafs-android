package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class InvoicePage extends AppCompatActivity {
    private RecyclerView rvPurchase;
    private MaterialButton payNowBtn, back_btn;
    private List<CartPage.Cart> cartList;
    private String email;
    private TextView totalTextView;
    private TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        // Receive product data from CartPage
        Intent intent = getIntent();
        cartList = intent.getParcelableArrayListExtra("cartList");

        // RecyclerView
        rvPurchase = findViewById(R.id.rvPurchase);
        // Change this line in your PurchasePage activity
        rvPurchase.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        PurchaseAdapter invoiceAdapter = new PurchaseAdapter(cartList);
        rvPurchase.setAdapter(invoiceAdapter);

        //get user email
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        //Total Harga
        totalTextView = findViewById(R.id.total);

        // Calculate and display the total
        totalTextView = findViewById(R.id.total);
        double totalAmount = calculateTotal();
        totalTextView.setText(formatCurrency(totalAmount));

        // Add address
        // TextView for the address
        address = findViewById(R.id.address);
        // Retrieve the address from the intent or any other source
        String addressText = getIntent().getStringExtra("addressKey");
        // Set the address in the TextView
        address.setText(addressText);

    }

    private double calculateTotal() {
        double total = 9500;

        // Sum the prices of all products in the cartList
        for (CartPage.Cart cartItem : cartList) {
            try {
                // Parse the product price to double and multiply by quantity
                double productPrice = Double.parseDouble(cartItem.getProduct_price().replaceAll("[^\\d.]", ""));
                total += productPrice * cartItem.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return total;
    }


    private String formatCurrency(double amount) {
        // Format the total amount as currency in Indonesian Rupiah (IDR)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("Rp #,###", symbols);

        return format.format(amount);
    }

    //send email


}