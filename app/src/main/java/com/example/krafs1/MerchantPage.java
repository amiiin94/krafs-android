package com.example.krafs1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MerchantPage extends AppCompatActivity {
    private LinearLayout homepage, navarticle, navprofile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_page);

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
                Intent profileIntent = new Intent(MerchantPage.this, LoginPage.class);
                startActivity(profileIntent);
            }
        });


        // Add any code specific to this activity here
    }
}
