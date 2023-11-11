package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ProfilePage extends AppCompatActivity {
    private LinearLayout homepage, navMerchant, navforum, navarticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        homepage = findViewById(R.id.homepage);
        navMerchant = findViewById(R.id.navmerchant);
        navforum = findViewById(R.id.navforum);
        navarticle = findViewById(R.id.navarticle);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ProfilePage.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        navMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent merchantIntent = new Intent(ProfilePage.this, MerchantPage.class);
                startActivity(merchantIntent);
            }
        });

        navforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent forumtIntent = new Intent(ProfilePage.this, ForumPage.class);
                startActivity(forumtIntent);
            }
        });


        navarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent articleIntent = new Intent(ProfilePage.this, ArticlePage.class);
                startActivity(articleIntent);
            }
        });


    }
}