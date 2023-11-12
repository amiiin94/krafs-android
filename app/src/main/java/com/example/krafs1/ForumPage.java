package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ForumPage extends AppCompatActivity {
    CardView forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_page);

        forum = findViewById(R.id.forum);

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ketika elemen navMerchant diklik
                Intent forumIntent = new Intent(ForumPage.this, ForumDetail.class);
                startActivity(forumIntent);
            }
        });
    }


}