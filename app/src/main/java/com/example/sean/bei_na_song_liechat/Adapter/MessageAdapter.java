package com.example.sean.bei_na_song_liechat.Adapter;

import com.example.sean.bei_na_song_liechat.Model.Chat;
import com.example.sean.bei_na_song_liechat.Model.ChatMessage;
import com.example.sean.bei_na_song_liechat.R;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sean.bei_na_song_liechat.MessageActivity;
import com.example.sean.bei_na_song_liechat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

//***************************** Part.8 Display message *****************************
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        ChatMessage message = chat.getMessage();
        switch (message.checkMessageType()){
            case "Image":
                holder.show_image.setImageResource(message.getImageMessage());
                holder.show_message.setVisibility(View.GONE);
                break;
            case "Text":
                holder.show_message.setText(message.getTextMessage());
                holder.show_image.setVisibility(View.GONE);
                break;
        }

        if (imageurl.equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

        if (position == mChat.size()-1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    //Get the total users count.
    @Override
    public int getItemCount() {
        return mChat.size();
    }

    //Construct the user item .
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView show_image;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            show_image = itemView.findViewById(R.id.show_image);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
//***************************** Part.8 Display message *****************************