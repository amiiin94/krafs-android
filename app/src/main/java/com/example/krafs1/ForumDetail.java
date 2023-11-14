package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForumDetail extends AppCompatActivity {

    private List<ForumDetail.Chat> chatList;

    private RecyclerView rvChat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_detail_page);

        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new GridLayoutManager(this, 1));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rvChat.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        chatList = new ArrayList<>();

        // Set an empty adapter
        ChatAdapter chatAdapter = new ChatAdapter(chatList);
        rvChat.setAdapter(chatAdapter);

        getAllchats();
        // Add any code specific to this activity here
    }


    public void getAllchats() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllChats";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray chats = new JSONArray(response);
                            for (int i = 0; i < chats.length(); i++) {
                                JSONObject chatJson = chats.getJSONObject(i);

                                String sender = chatJson.getString("sender");
                                String chat = chatJson.getString("chat");
                                String time = chatJson.getString("time");

                                // Menambahkan chat ke dalam chatList
                                ForumDetail.Chat chatObject = new ForumDetail.Chat(sender, chat, time);
                                chatList.add(chatObject);
                            }
                            displaychats();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForumDetail.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private void displaychats() {
        ChatAdapter chatAdapter = (ChatAdapter) rvChat.getAdapter();
        chatAdapter.updateChatList(chatList);
    }







    public static class Chat {
        private String sender;
        private String chat;
        private String time;

        public Chat(String sender, String chat, String time) {
            this.sender = sender;
            this.chat = chat;
            this.time = time;
        }

        public String getSender() {
            return sender;
        }

        public String getChat() {
            return chat;
        }

        public String getTime() {
            return time;
        }
    }
}