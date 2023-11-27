package com.example.krafs1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends AppCompatActivity{
    private RecyclerView rvCart;
    private List<Cart> cartList;
    private String user_id;
    private String product_id;
    private Button order_btn;
    public List<Cart> getProductData() {
        return cartList;
    };
    private ImageView delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);


        order_btn = findViewById(R.id.order_btn);

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartPage.this, PurchasePage.class);
                intent.putExtra("cartList", new ArrayList<>(cartList));
                startActivity(intent);
            }
        });

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
                            List<String> cartIds = new ArrayList<>();
                            List<String> productIds = new ArrayList<>();
                            List<Integer> quantityList = new ArrayList<>();

                            for (int i = 0; i < carts.length(); i++) {
                                JSONObject cartJson = carts.getJSONObject(i);

                                String cart_id = cartJson.getString("_id");
                                product_id = cartJson.getString("product_id");
                                int quantity = cartJson.getInt("quantity");

                                // Store the product_id in the list
                                cartIds.add(cart_id);
                                productIds.add(product_id);
                                quantityList.add(quantity);
                            }
                            getProductDetail(productIds, quantityList ,cartIds , new CartCallback() {
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

    private void getProductDetail(List<String> productIds, List<Integer> quantityList, List<String> cartIds, final CartCallback callback) {
        int[] requestsCompleted = {0};

        for (int i = 0; i < productIds.size(); i++) {
            final String product_id = productIds.get(i);
            final int quantity = quantityList.get(i);
            final String cartId = cartIds.get(i);

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
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                Cart cart = new Cart(idp, product_name, product_price, quantity, cartId, imgUrl);
                                cartList.add(cart);

                                requestsCompleted[0]++;

                                if (requestsCompleted[0] == productIds.size()) {
                                    callback.onCartListReady((cartList));

                                    displayProducts(cartList);
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
    }

    private void displayProducts(List<Cart> cartList) {
        CartAdapter cartAdapter = new CartAdapter(cartList,CartPage.this);
        rvCart.setAdapter(cartAdapter);
    }

//    DELETE

    public void delete(String productId, String cartId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/deleteCartById?id=" + cartId;

        // Create a StringRequest with DELETE method
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion, e.g., remove the item from the cartList
                        removeItemFromCartList(productId);
                        // Update the RecyclerView
                        displayProducts(cartList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartPage.this, "Error deleting item: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void removeItemFromCartList(String productId) {
        for (Cart cart : cartList) {
            if (cart.getIdp().equals(productId)) {
                cartList.remove(cart);
                break;
            }
        }
    }

    public interface CartCallback {
        void onCartListReady(List<Cart> cartList);
        void onError(String errorMessage);
    }


    public static class Cart implements Parcelable {
        private String idp;
        private String product_name;
        private String product_price;
        private int quantity;
        private String idcart;
        private String imageUrl;

        public Cart(String idp, String product_name, String product_price, int quantity, String idcart, String imageUrl) {
            this.idp = idp;
            this.product_name = product_name;
            this.product_price = product_price;
            this.quantity = quantity;
            this.idcart = idcart;
            this.imageUrl = imageUrl;
        }

        protected Cart(Parcel in) {
            idp = in.readString();
            product_name = in.readString();
            product_price = in.readString();
            quantity = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(idp);
            dest.writeString(product_name);
            dest.writeString(product_price);
            dest.writeInt(quantity);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Cart> CREATOR = new Creator<Cart>() {
            @Override
            public Cart createFromParcel(Parcel in) {
                return new Cart(in);
            }

            @Override
            public Cart[] newArray(int size) {
                return new Cart[size];
            }
        };

        // Getter methods
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
        public String getIdcart() {
            return idcart;
        }

        // Setter method for quantity
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
    public void editQuantityByIdproduct(String idcart, int quantity) {

        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/editQuantityByIdproduct?id=" +idcart+ "&quantity="+quantity;

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest with PUT method
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CartPage.this, "Update Quantity successful!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartPage.this, "Error Quantity profile: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}