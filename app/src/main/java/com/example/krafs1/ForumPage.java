package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ForumPage extends AppCompatActivity {
    CardView forum;

    private LinearLayout homepage, navMerchant, navarticle, navprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_page);

        forum = findViewById(R.id.forum);

        //navigation for bottom bar
        homepage = findViewById(R.id.homepage);
        navMerchant = findViewById(R.id.navmerchant);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent intent = new Intent(ForumPage.this, MerchantPage.class);
                startActivity(intent);
            }
        });

        navMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent intent = new Intent(ForumPage.this, MerchantPage.class);
                startActivity(intent);
            }
        });

        navarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent intent = new Intent(ForumPage.this, ArticlePage.class);
                startActivity(intent);
            }
        });

        navprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(ForumPage.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(ForumPage.this, ProfilePage.class);
                    startActivity(profileIntent);
                }
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(ForumPage.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(ForumPage.this, ForumDetail.class);
                    startActivity(profileIntent);
                }
            }
        });
    }


}