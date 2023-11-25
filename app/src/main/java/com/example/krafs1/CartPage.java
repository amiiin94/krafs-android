package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CartPage extends AppCompatActivity {
    private RecyclerView rv;
    private List<Cart> cartList;
    private String user_id;
    private String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_product);

        // Get user_id from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        // RecyclerView
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rv.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        cartList = new ArrayList<>();

        getProductByUserId();
    }

    private void getProductByUserId() {
        String urlEndPoints = "https://your-api-url.com/getCart?user_id=" + user_id;

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray carts = new JSONArray(response);

                            // Create a list to store all product_ids
                            List<String> productIds = new ArrayList<>();

                            for (int i = 0; i < carts.length(); i++) {
                                JSONObject cartJson = carts.getJSONObject(i);

                                product_id = cartJson.getString("product_id");
                                int quantity = cartJson.getInt("quantity");

                                // Store the product_id in the list
                                productIds.add(product_id);
                            }
                            getProductDetail(productIds);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    private void getProductDetail(List<String> productIds) {
        for (String productId : productIds) {
            String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductByMoreThanOneId?id=" + productId;

            StringRequest sr = new StringRequest(
                    Request.Method.GET,
                    urlEndPoints,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray carts = new JSONArray(response);
                                for (int i = 0; i < carts.length(); i++) {
                                    JSONObject cartJson = carts.getJSONObject(i);

                                    String idp = cartJson.getString("_id");
                                    String product_name = cartJson.getString("name");
                                    String product_price = cartJson.getString("price");
                                    int quantity = cartJson.getInt("quantity");

                                    Cart cart = new Cart(idp, product_name, product_price, quantity);
                                    cartList.add(cart);
                                }

                                // Move the adapter creation and setting outside the loop
                                CartAdapter cartAdapter = new CartAdapter(cartList);
                                rv.setAdapter(cartAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CartPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(sr);
        }
    }


    static class Cart {
        private String idp;
        private String product_name;
        private String product_price;
        private int quantity;

        public Cart(String idp, String product_name, String product_price, int quantity) {
            this.idp = idp;
            this.product_name = product_name;
            this.product_price = product_price;
            this.quantity = quantity;
        }

        public String getIdp() {
            return idp;
        }

        public String getProduct_name() {
            return product_name;
        }

        public String getProduct_price() {
            return product_price;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}