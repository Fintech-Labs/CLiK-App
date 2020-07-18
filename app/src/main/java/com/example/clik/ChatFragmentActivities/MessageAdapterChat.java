package com.example.clik.ChatFragmentActivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapterChat extends RecyclerView.Adapter<MessageAdapterChat.ViewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private List<Chat> chatList;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapterChat(Context context, List<Chat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapterChat.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapterChat.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterChat.ViewHolder holder, int position) {

        Chat chat=chatList.get(position);
        holder.show_message.setText(chat.getMessage());
        Picasso.get().load(imageUrl).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
