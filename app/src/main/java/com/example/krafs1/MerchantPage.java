package com.example.krafs1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MerchantPage extends AppCompatActivity {
    private LinearLayout homepage, navarticle, navprofile;
    private List<Product> productList;
    private RecyclerView rv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_page);

        rv1 = findViewById(R.id.rv1);

        rv1.setLayoutManager(new GridLayoutManager(this, 2));

        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rv1.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));


        productList = new ArrayList<>();

        homepage = findViewById(R.id.homepage);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);

        getAllProducts();

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MerchantPage.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
        navarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent articleIntent = new Intent(MerchantPage.this, ArticlePage.class);
                startActivity(articleIntent);
            }
        });

        navprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MerchantPage.this, LoginPage.class);
                startActivity(profileIntent);
            }
        });
        // Add any code specific to this activity here
    }

    public void getAllProducts() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllProducts";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productJson = products.getJSONObject(i);

                                String nama = productJson.getString("name");
                                int price = productJson.getInt("price");
                                String formattedHarga = formatToRupiah(price);

                                // Mengambil URL gambar
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                // Menambahkan produk ke dalam productList
//
                                Product product = new Product(nama, formattedHarga, imgUrl);

//                                List<Product> productList = new ArrayList<>();
                                productList.add(product);
                            }
                            displayProducts();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    private String formatToRupiah(int value) {
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        formatRupiah.setCurrency(Currency.getInstance("IDR"));

                        String formattedValue = formatRupiah.format(value).replace("Rp", "").trim();

                        return "Rp. " + formattedValue;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MerchantPage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
    private void displayProducts() {
        ProductAdapter productAdapter = new ProductAdapter(productList);
        rv1.setAdapter(productAdapter);
    }

    public static class Product {
        private String name;
        private String price;
        private String imageUrl;

        public Product(String name, String price, String imageUrl) {
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}