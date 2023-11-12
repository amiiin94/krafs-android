package com.example.krafs1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MerchantPage extends AppCompatActivity {
    private LinearLayout homepage, navarticle, navprofile;
    private TextView tv1Nama, tv1Harga, tv2Nama;
    private ImageView iv1;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_page);

        tableLayout = findViewById(R.id.tableLayout);

        productList = new ArrayList<>();

        tv1Nama = findViewById(R.id.tv1Nama);
        tv1Harga = findViewById(R.id.tv1Harga);
        iv1 = findViewById(R.id.iv1);
        tv2Nama = findViewById(R.id.tv2Nama);
        homepage = findViewById(R.id.homepage);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);



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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(MerchantPage.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(MerchantPage.this, ProfilePage.class);
                    startActivity(profileIntent);
                }
            }
        });

        getAllProduts();
        // Add any code specific to this activity here
    }

    public void getAllProduts() {
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
                                productList.add(new Product(nama, formattedHarga, imgUrl));
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
        for (Product product : productList) {
            TableRow row = new TableRow(this);

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(this);
            Picasso.get().load(product.getImageUrl()).into(imageView);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(product.getName());

            TextView priceTextView = new TextView(this);
            priceTextView.setText(product.getPrice());

            linearLayout.addView(imageView);
            linearLayout.addView(nameTextView);
            linearLayout.addView(priceTextView);

            cardView.addView(linearLayout);
            row.addView(cardView);
            tableLayout.addView(row);
        }
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
