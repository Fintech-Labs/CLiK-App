package com.example.clik.ChatFragmentActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapterChat extends RecyclerView.Adapter<UserAdapterChat.ViewHolder>{
    private Context context;
    private List<UserChat> userChatList;

    public UserAdapterChat(Context context, List<UserChat> userChatList) {
        this.context = context;
        this.userChatList = userChatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item_chat,parent,false);
        return new UserAdapterChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserChat userChat=userChatList.get(position);
        holder.userName.setText(userChat.getUsername());
        Picasso.get().load(userChat.getImageURL()).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }
}