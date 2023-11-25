package com.example.krafs1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends AppCompatActivity {
    private RecyclerView rvCart;
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
        rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new GridLayoutManager(this, 1));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rvCart.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        cartList = new ArrayList<>();

        getProductByUserId();
    }

    private void getProductByUserId() {
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
                            List<String> productIds = new ArrayList<>();
                            List<Integer> quantityList = new ArrayList<>();

                            for (int i = 0; i < carts.length(); i++) {
                                JSONObject cartJson = carts.getJSONObject(i);

                                product_id = cartJson.getString("product_id");
                                int quantity = cartJson.getInt("quantity");

                                // Store the product_id in the list
                                productIds.add(product_id);
                                quantityList.add(quantity);
                                Log.d("ini cart : ", String.valueOf(quantity));
                            }
                            getProductDetail(productIds, quantityList, new CartCallback() {
                                @Override
                                public void onCartListReady(List<Cart> cartList) {
                                    displayProducts(cartList);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(CartPage.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });

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

    private void getProductDetail(List<String> productIds, List<Integer> quantityList, final CartCallback callback) {
        for (int i = 0; i < productIds.size(); i++) {
            final String product_id = productIds.get(i);
            final int quantity = quantityList.get(i);

            // Bangun URL untuk setiap ID produk
            String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductByMoreThanOneId?id=" + product_id;

            StringRequest sr = new StringRequest(
                    Request.Method.GET,
                    urlEndPoints,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray products = new JSONArray(response);
                                JSONObject productJson = products.getJSONObject(0);

                                String idp = productJson.getString("_id");
                                String product_name = productJson.getString("name");
                                String product_price = productJson.getString("price");

//                                int quantity =

                                Cart cart = new Cart(idp, product_name, product_price, quantity);
                                cartList.add(cart);

                                if (cartList.size() == productIds.size()) {
                                    callback.onCartListReady(cartList);
                                }

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

            // Tambahkan permintaan ke RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(sr);
        }
        // Pindahkan pembuatan dan pengaturan adapter di luar loop jika ingin mengatur adapter setelah semua permintaan selesai
        displayProducts(cartList);
    }

//    private int getProductQuantity (final QuantityCallback callback) {
//        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getCartsByUserId?user_id=" + user_id;
//
//        StringRequest sr = new StringRequest(
//                Request.Method.GET,
//                urlEndPoints,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONArray carts = new JSONArray(response);
//
//                            // Create a list to store all product_ids
//                            List<String> productIds = new ArrayList<>();
//
//                            for (int i = 0; i < carts.length(); i++) {
//                                JSONObject cartJson = carts.getJSONObject(i);
//
//                                product_id = cartJson.getString("product_id");
//                                int quantity = cartJson.getInt("quantity");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(CartPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        return quantity;
//    }
    private void displayProducts(List<Cart> cartList) {
        CartAdapter cartAdapter = new CartAdapter(cartList);
        rvCart.setAdapter(cartAdapter);
    }

//    public interface QuantityCallback {
//        void onQuantityReady(int quantity);
//        void onError(String errorMessage);
//    }

    public interface CartCallback {
        void onCartListReady(List<Cart> cartList);
        void onError(String errorMessage);
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