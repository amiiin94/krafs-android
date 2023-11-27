package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {
    private LinearLayout homepage, navMerchant, navforum, navarticle;
    private Button logout, edit_profile_btn, cart_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        homepage = findViewById(R.id.homepage);
        navMerchant = findViewById(R.id.navmerchant);
        navforum = findViewById(R.id.navforum);
        navarticle = findViewById(R.id.navarticle);
        logout = findViewById(R.id.logout);
        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        cart_btn = findViewById(R.id.cart_btn);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String notelp = sharedPreferences.getString("notelp", "");

        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView notelpTextView = findViewById(R.id.notelp);

        usernameTextView.setText(username);
        emailTextView.setText(email);
        notelpTextView.setText(notelp);

        if (!username.isEmpty()) {
            usernameTextView.setText(username);
        } else {
            usernameTextView.setText("Default Username");
        }

// Check if email is not empty before setting it to TextView
        if (!email.isEmpty()) {
            emailTextView.setText(email);
        } else {
            emailTextView.setText("Default Email");
        }

// Check if notelp is not empty before setting it to TextView
        if (!notelp.isEmpty()) {
            notelpTextView.setText(notelp);
        } else {
            notelpTextView.setText("Default Notelp");
        }

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, CartPage.class);
                startActivity(intent);
            }
        });

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, ProfileEdit.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.clear();
                editor.apply();

                Intent homeIntent = new Intent(ProfilePage.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

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
            public void onClick(View view) {
                Intent forumIntent = new Intent(ProfilePage.this, ForumPage.class);
                startActivity(forumIntent);
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