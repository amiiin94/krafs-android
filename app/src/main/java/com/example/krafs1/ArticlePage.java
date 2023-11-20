package com.example.krafs1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ArticlePage extends AppCompatActivity {
    private LinearLayout homepage, navmerchant, navforum, navprofile;

    private List<Article> articleList;
    private RecyclerView rv;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_page);

        homepage = findViewById(R.id.homepage);
        navmerchant = findViewById(R.id.navmerchant);
        navforum = findViewById(R.id.navforum);
        navprofile = findViewById(R.id.navprofile);

        //RecycleView
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rv.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        articleList = new ArrayList<>();

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ArticlePage.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        navmerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent merchantIntent = new Intent(ArticlePage.this, MerchantPage.class);
                startActivity(merchantIntent);
            }
        });

        navforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forumIntent = new Intent(ArticlePage.this, ForumPage.class);
                startActivity(forumIntent);
            }
        });

        navprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(ArticlePage.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(ArticlePage.this, ProfilePage.class);
                    startActivity(profileIntent);
                }
            }
        });

        getAllArticles();
    }

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
                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject articleJson = articles.getJSONObject(i);

                                String idp = articleJson.getString("_id");
                                String title = articleJson.getString("title");
                                String content = articleJson.getString("content");
                                String imageUrl = articleJson.getString("img");

                                // Create an Article object and add it to the list
                                Article article = new Article(idp, title, content, imageUrl);
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
                        Toast.makeText(ArticlePage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private void displayArticle() {
        // ... (existing code)

        // Create an instance of ArticleAdapter
        ArticleAdapter articleAdapter = new ArticleAdapter(articleList);
        rv.setAdapter(articleAdapter);  // Set the adapter to the RecyclerView
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
