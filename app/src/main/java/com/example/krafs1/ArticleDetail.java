package com.example.krafs1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticleDetail extends AppCompatActivity {
    TextView title, content;
    ImageView image, backArtikel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_page);

        backArtikel = findViewById(R.id.backArtikel);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ARTICLE_ID")) {
            String articleId = intent.getStringExtra("ARTICLE_ID");
            Log.d("ArticleDetail", "Received ARTICLE_ID: " + articleId);

            title = findViewById(R.id.title);
            content = findViewById(R.id.content);
            image = findViewById(R.id.image);

            getArticleById(articleId);
        } else {
            // Penanganan jika ID tidak diterima dengan benar
            Log.d("ArticleDetail", "ARTICLE_ID not found");
            Toast.makeText(this, "ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Anda mungkin ingin menutup aktivitas ini jika ID tidak ditemukan
        }
        backArtikel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backArtikelIntent = new Intent(ArticleDetail.this, ArticlePage.class);
                startActivity(backArtikelIntent);
            }
        }));

    }

    public void getArticleById(String articleId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getarticlebyid?id=" + articleId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray articles = new JSONArray(response);

                            if (articles.length() > 0) {
                                // Assuming you want to get the first article in the array
                                JSONObject articleJson = articles.getJSONObject(0);

                                String idp = articleJson.getString("_id");
                                String title = articleJson.getString("title");
                                String content = articleJson.getString("content");

                                // Taro data di id textview
                                ArticleDetail.this.title.setText(title);
                                ArticleDetail.this.content.setText(content);
                            } else {
                                // Handle the case where no article is found
                                Log.d("ArticleDetail", "No article found for ARTICLE_ID: " + articleId);
                                Toast.makeText(ArticleDetail.this, "Article not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ArticleDetail.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}