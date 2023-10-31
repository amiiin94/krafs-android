package com.example.krafs1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout navMerchant, navarticle, navprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navMerchant = findViewById(R.id.navmerchant);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);


        // Menambahkan OnClickListener ke elemen navMerchant
        navMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent merchantIntent = new Intent(MainActivity.this, MerchantPage.class);
                startActivity(merchantIntent);
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
                Intent profileIntent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(profileIntent);
            }
        });
    }
}
