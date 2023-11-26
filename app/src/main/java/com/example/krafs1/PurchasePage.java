package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    private EditText address;
    private List<CartPage.Cart> cartList;
    private TextView totalTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);

        //address
        address = findViewById(R.id.address);

        //pay now button
        payNowBtn = findViewById(R.id.pay_now_btn);
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start InvoicePage activity
                Intent invoiceIntent = new Intent(PurchasePage.this, InvoicePage.class);
                invoiceIntent.putParcelableArrayListExtra("cartList", new ArrayList<>(cartList));
                invoiceIntent.putExtra("addressKey", address.getText().toString());
                startActivity(invoiceIntent);

            }
        });

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
        cartList = intent.getParcelableArrayListExtra("cartList");

        // RecyclerView
        rvPurchase = findViewById(R.id.rvPurchase);
        // Change this line in your PurchasePage activity
        rvPurchase.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        PurchaseAdapter purchaseAdapter = new PurchaseAdapter(cartList);
        rvPurchase.setAdapter(purchaseAdapter);

        // Calculate and display the total
        totalTextView = findViewById(R.id.total);
        double totalAmount = calculateTotal();
        totalTextView.setText(formatCurrency(totalAmount));

        // Add Address From EditText
        address = findViewById(R.id.address);
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

    private void sendEmail() {
        // Get the recipient email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String recipientEmail = sharedPreferences.getString("email", "");

        // Get the subject for the email
        String subject = "Invoice for your purchase";

        // Build the email body
        StringBuilder emailBody = new StringBuilder();
        for (CartPage.Cart cartItem : cartList) {
            emailBody.append("Product: ").append(cartItem.getProduct_name()).append("\n");
            emailBody.append("Price: ").append(cartItem.getProduct_price()).append("\n");
            emailBody.append("Quantity: ").append(cartItem.getQuantity()).append("\n");
            emailBody.append("\n");
        }

        // Add total and delivery address
        double totalAmount = calculateTotal();
        emailBody.append("Total: ").append(formatCurrency(totalAmount)).append("\n");
        emailBody.append("Delivery Address: ").append(address.getText().toString()).append("\n\n");

        // Add any additional information or closing text
        emailBody.append("Thank you for your purchase!\n");

        // Execute the SendMail AsyncTask
        SendMail sendMail = new SendMail(recipientEmail, subject, emailBody.toString());
        sendMail.execute();
    }

}
