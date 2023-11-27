package com.example.krafs1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

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

                sendEmail();
                if (cartList != null) {
                    for (int i = 0; i < cartList.size(); i++) {
                        CartPage.Cart cart = cartList.get(i);

                        // Dapatkan idp dari objek CartPage.Cart
//                        String idp = cart.getIdp();
                        String idc = cart.getIdcart();
                        if (idc != null) {
                            delete(idc);
                        }else {
                            String tes123 = "kosong bro 123 :";
                            Log.d("",tes123);
                        }
                        // Lakukan sesuatu dengan idp, misalnya cetak ke log
                    }
                }

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
            emailBody.append("Product: ").append(cartItem.getProduct_name()).append("<br>");
            emailBody.append("Price: ").append( cartItem.getProduct_price()).append("<br>");
            emailBody.append("Quantity: ").append(cartItem.getQuantity()).append("<br>");
            emailBody.append("<br>");
        }

        emailBody.append("Ongkos kirim: Rp 9.500").append("<br><br>");

        // Add total and delivery address
        double totalAmount = calculateTotal();
        emailBody.append("Total: ").append(formatCurrency(totalAmount + 9500)).append("<br>");
        emailBody.append("Delivery Address: ").append(address.getText().toString()).append("<br><br>");

        //bank and emoney
        emailBody.append("Pembayaran bisa dilakukan pada nomor rekening dan nomor telepon berikut ini:").append("<br>");
        emailBody.append("Bank BRI: 034101000743127 <br>");
        emailBody.append("Bank BCA: 326201012057123 <br>");
        emailBody.append("Bank BNI: 478201003567562 <br>");
        emailBody.append("Bank Dana: 085156266406 <br>");
        emailBody.append("Bank Gopay: 085156266406 <br>");
        emailBody.append("Bank ShopeePay: 085156266406 <br><br>");

        emailBody.append("Go to this <a href='https://forms.gle/uxEaH7NgoDJzw46TA'>link</a> for your payment confirmation. <br><br>");

        // Add any additional information or closing text
        emailBody.append("Thank you for your purchase!<br>");

        // Execute the SendMail AsyncTask
        SendMail sendMail = new SendMail(recipientEmail, subject, emailBody.toString());
        sendMail.execute();

        Toast.makeText(PurchasePage.this, "See email for details", Toast.LENGTH_SHORT).show();
    }

//    DELETE

    public void delete(String cartId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/deleteCartById?id=" + cartId;

        // Create a StringRequest with DELETE method
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion, e.g., remove the item from the cartList
                        // Update the RecyclerView
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PurchasePage.this, "Error deleting item: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
