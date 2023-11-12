package com.example.krafs1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ArticlePage extends AppCompatActivity {
    private LinearLayout homepage, navmerchant, navarticle, navprofile;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_page);

        homepage = findViewById(R.id.homepage);
        navmerchant = findViewById(R.id.navmerchant);
        navprofile = findViewById(R.id.navprofile);

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
    }





}
