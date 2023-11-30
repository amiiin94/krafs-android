package com.example.krafs1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {
    private TextView nama_produk, harga_produk, stok_produk, deskripsi_produk;
    private ImageView foto_produk, backProduct;
    private Button cart_btn;
    private String user_id;
    private ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        backProduct = findViewById(R.id.backProduct);
        // Retrieve user_id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", user_id);

        // Menerima ID dari Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            String productId = intent.getStringExtra("PRODUCT_ID");

            foto_produk = findViewById(R.id.foto_produk);
            nama_produk = findViewById(R.id.nama_produk);
            harga_produk = findViewById(R.id.harga_produk);
            stok_produk = findViewById(R.id.stok_produk);
            deskripsi_produk = findViewById(R.id.deskripsi_produk);
            cart_btn = findViewById(R.id.cart_btn);

            getProductById(productId);
        } else {
            Toast.makeText(this, "ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productModel != null) {
                    insertProductToCart(user_id, productModel);
                } else {
                    Toast.makeText(ProductDetail.this, "Product not loaded yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backProduct.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backProductIntent = new Intent(ProductDetail.this, MerchantPage.class);
                startActivity(backProductIntent);
            }
        }));
    }

    private void getCartByUserId() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getCartsByUserId?user_id=" + user_id;

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray carts = new JSONArray(response);

                            // Create a list to store all product_ids
                            List<String> cartIds = new ArrayList<>();
                            List<String> productIds = new ArrayList<>();
                            List<Integer> quantityList = new ArrayList<>();

                            for (int i = 0; i < carts.length(); i++) {
                                JSONObject cartJson = carts.getJSONObject(i);

                                String cart_id = cartJson.getString("_id");
                                String product_id = cartJson.getString("product_id");
                                int quantity = cartJson.getInt("quantity");

                                // Store the product_id in the list
                                cartIds.add(cart_id);
                                productIds.add(product_id);
                                quantityList.add(quantity);
                            }
//                            getCartByUserId(productIds, quantityList ,cartIds , new CartPage.CartCallback() {
//                                @Override
//                                public void onError(String errorMessage) {
//                                    Toast.makeText(ProductDetail.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    public void getProductById(String productId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductById?id=" + productId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            JSONObject productJson = products.getJSONObject(0);

                            // Initialize productModel with the product details
                            productModel = new ProductModel(productJson);
                            productModel.setId(productId); // Set the id received from the database

                            Picasso.get().load(productModel.getImageUrl()).into(foto_produk);
                            nama_produk.setText(productModel.getName());
                            harga_produk.setText(productModel.getFormattedPrice());
                            stok_produk.setText("Stock: " + productModel.getStock() + " Pcs");
                            deskripsi_produk.setText(productModel.getDescription());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void insertProductToCart(String user_id, ProductModel productModel) {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/insertProductToCartByUserId" +
                "?user_id=" + user_id +
                "&product_id=" + productModel.getId();

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check if the response contains an error field
                            if (jsonResponse.has("error")) {
                                String errorMessage = jsonResponse.getString("error");
                                // Display toast with the error message
                                Toast.makeText(ProductDetail.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Registration successful
                                Toast.makeText(ProductDetail.this, "Added Product to Cart!", Toast.LENGTH_SHORT).show();
                                //Intent loginIntent = new Intent(ProductDetail.this, LoginPage.class);
                                //startActivity(loginIntent);
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            e.printStackTrace();
                            Toast.makeText(ProductDetail.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
}
