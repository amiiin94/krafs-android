package com.example.krafs1;

import android.content.Context;
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

    public ChatAdapter(List<ForumDetail.Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View chatView = inflater.inflate(R.layout.chat_forum, parent, false);

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

            // Get references to the views defined in chat_forum.xml
            sender = itemView.findViewById(R.id.sender);
            chat = itemView.findViewById(R.id.chat);
            time = itemView.findViewById(R.id.time);
        }

    }
}
