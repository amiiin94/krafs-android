package com.example.krafs1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LinearLayout navMerchant, navarticle, navforum, navprofile;
    private Button join_our_community;
    private TextView selamatDatang;
    private List<MerchantPage.Product> productList;
    private List<ArticlePage.Article> articleList;
    private RecyclerView rvProduct, rvArticles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navMerchant = findViewById(R.id.navmerchant);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);
        navforum = findViewById(R.id.navforum);

        productList = new ArrayList<>();
        articleList = new ArrayList<>();

        rvProduct = findViewById(R.id.rvProduct);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProduct.setLayoutManager(layoutManager);

        rvArticles = findViewById(R.id.rvArticles);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvArticles.setLayoutManager(layoutManager2);

        join_our_community = findViewById(R.id.join_our_community);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");

        TextView textSelamatDatang = findViewById(R.id.selamatDatang);
        if (!username.isEmpty()) {
            textSelamatDatang.setText("Hai " + username);
        } else {
            textSelamatDatang.setText("Welcome to KRAFS");
        }

        join_our_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(MainActivity.this, ForumDetail.class);
                    startActivity(profileIntent);
                }
            }
        });

        // Menambahkan OnClickListener ke elemen navMerchant
        navMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent merchantIntent = new Intent(MainActivity.this, MerchantPage.class);
                startActivity(merchantIntent);
            }
        });

        navforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forumIntent = new Intent(MainActivity.this, ForumPage.class);
                startActivity(forumIntent);
            }
        });

        navarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent articleIntent = new Intent(MainActivity.this, ArticlePage.class);
                startActivity(articleIntent);
            }
        });

        navprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(MainActivity.this, ProfilePage.class);
                    startActivity(profileIntent);
                }
            }
        });

        getAllProductsSortUpdate();
        getAllArticles();

    }
    public void getAllProductsSortUpdate() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductsSortCreate";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for (int i = 0; i < 4; i++) {
                                JSONObject productJson = products.getJSONObject(i);

                                String idp = productJson.getString("_id");
                                String nama = productJson.getString("name");
                                int price = productJson.getInt("price");
                                String formattedHarga = formatToRupiah(price);

                                // Mengambil URL gambar
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                // Menambahkan produk ke dalam productList
//
                                MerchantPage.Product product = new MerchantPage.Product(idp, nama, formattedHarga, imgUrl);

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
                        Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
    private void displayProducts() {
        ProductAdapterHome productAdapterHome = new ProductAdapterHome(productList);
        rvProduct.setAdapter(productAdapterHome);
    }

    public static class Product {
        private  String idp;
        private String name;
        private String price;
        private String imageUrl;

        public Product(String idp ,String name, String price, String imageUrl) {
            this.idp = idp;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public  String getIdp() {return idp;}

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

//    Articles

    public void getAllArticles() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllArticles";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray articles = new JSONArray(response);
                            for (int i = 0; i < 4; i++) {
                                JSONObject articleJson = articles.getJSONObject(i);

                                String idp = articleJson.getString("_id");
                                String title = articleJson.getString("title");
                                String content = articleJson.getString("content");
                                String imageUrl = articleJson.getString("img");

                                int jumlahKata = 5;
                                int jumlahKata2 = 15;
                                String[] konten = content.split("\\s+");
                                String[] judul = title.split("\\s+");

                                StringBuilder contentToShow = new StringBuilder();
                                for (int j = 0; j < Math.min(jumlahKata2, konten.length); j++) {
                                    contentToShow.append(konten[j]).append(" ");
                                }
                                StringBuilder tittleToShow = new StringBuilder();
                                for (int j = 0; j < Math.min(jumlahKata, judul.length); j++) {
                                    tittleToShow.append(judul[j]).append(" ");
                                }
                                String contentfix = ""+contentToShow+"...";

                                // Create an Article object and add it to the list
                                ArticlePage.Article article = new ArticlePage.Article(idp, title, contentfix, imageUrl);
                                articleList.add(article);
                            }
                            displayArticle();  // Correct method name
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private void displayArticle() {
        // ... (existing code)

        // Create an instance of ArticleAdapter
        ArticleAdapterHome articleAdapterHome = new ArticleAdapterHome(articleList);
        rvArticles.setAdapter(articleAdapterHome);  // Set the adapter to the RecyclerView
    }

    public static class Article {
        private String idp;
        private String title;
        private String content;
        private String imageUrl;

        public Article(String idp, String title, String content, String imageUrl) {
            this.idp = idp;
            this.title = title;
            this.content = content;
            this.imageUrl = imageUrl;
        }

        public String getIdp() {
            return idp;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
