package com.example.krafs1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout navMerchant, navarticle, navforum, navprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navMerchant = findViewById(R.id.navmerchant);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);
        navforum = findViewById(R.id.navforum);


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

    }
}
