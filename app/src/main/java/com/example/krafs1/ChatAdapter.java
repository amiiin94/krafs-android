package com.example.krafs1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ForumDetail.Chat> chatList;
    private Context context;
    private String username;

    public ChatAdapter(List<ForumDetail.Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        this.username = sharedPreferences.getString("username", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout based on the sender's username
        View chatView;
        if (viewType == 0) {
            // Layout for the current user
            chatView = inflater.inflate(R.layout.item_my_chat_forum, parent, false);
        } else {
            // Layout for other users
            chatView = inflater.inflate(R.layout.item_chat_forum, parent, false);
        }

        // Return a new holder instance
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        ForumDetail.Chat chat = chatList.get(position);

        // Set item views based on your views and data model
        holder.sender.setText(chat.getSender());
        holder.chat.setText(chat.getChat());
        holder.time.setText(String.valueOf(chat.getTime()));
    }

    @Override
    public int getItemViewType(int position) {
        // Return 0 if the sender is the current user, 1 otherwise
        return chatList.get(position).getSender().equals(username) ? 0 : 1;
    }

    public void updateChatList(List<ForumDetail.Chat> newChatList) {
        this.chatList = newChatList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sender;
        TextView chat;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in the corresponding layout
            sender = itemView.findViewById(R.id.sender);
            chat = itemView.findViewById(R.id.chat);
            time = itemView.findViewById(R.id.time);
        }
    }
}
