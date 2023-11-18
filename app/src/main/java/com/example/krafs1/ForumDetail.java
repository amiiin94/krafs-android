package com.example.krafs1;

import android.content.Context;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.List;

public class ForumDetail extends AppCompatActivity {

    private List<Chat> chatList;
    private RecyclerView rvChat;
    private EditText message_input;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_detail_page);

        message_input = findViewById(R.id.message_input);
        send = findViewById(R.id.send);

        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new GridLayoutManager(this, 1));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rvChat.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        //Get sender from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String sender = sharedPreferences.getString("username", "");

        chatList = new ArrayList<>();

        message_input = findViewById(R.id.message_input);


        // Set an empty adapter
        ChatAdapter chatAdapter = new ChatAdapter(chatList, this);
        rvChat.setAdapter(chatAdapter);

        getAllChats();

        //when click send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredMessageInput = message_input.getText().toString();

                sendMessage(sender, enteredMessageInput);

                //make input in message_input disappear
                message_input.setText("");
            }
        });
    }

    public void getAllChats() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllChats";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Clear existing messages before adding new ones
                            chatList.clear();

                            JSONArray chats = new JSONArray(response);
                            for (int i = 0; i < chats.length(); i++) {
                                JSONObject chatJson = chats.getJSONObject(i);

                                String sender = chatJson.getString("sender");
                                String chat = chatJson.getString("chat");
                                String time = chatJson.getString("time");

                                // Add chat to chatList
                                Chat chatObject = new Chat(sender, chat, time);
                                chatList.add(chatObject);
                            }
                            displayChats();
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


    private void displayChats() {
        ChatAdapter chatAdapter = (ChatAdapter) rvChat.getAdapter();
        if (chatAdapter != null) {
            chatAdapter.updateChatList(chatList);
        }
    }

    public void sendMessage(String sender, String message) {
        // Modify the URL and parameters as needed
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/sendMessageinForum" +
                "?sender=" + sender +
                "&chat=" + message;

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAllChats();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForumDetail.this, "Message sending failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
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