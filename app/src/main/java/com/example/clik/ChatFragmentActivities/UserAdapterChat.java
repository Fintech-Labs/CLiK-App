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

import com.example.clik.Model.ShowChats;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapterChat extends RecyclerView.Adapter<UserAdapterChat.ViewHolder>{
    private Context context;
    private List<User> userChatList;
    private boolean isChat;

    public UserAdapterChat(Context context, List<User> userChatList,boolean isChat) {
        this.context = context;
        this.userChatList = userChatList;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item_chat,parent,false);
        return new UserAdapterChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final User user=userChatList.get(position);
        holder.userName.setText(user.getName());
        Picasso.get().load(user.getProfileUri()).into(holder.profile_image);

        FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference("ChatUsers").child(mUser.getUid())
                .child(user.getuId());

        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ShowChats showChats=snapshot.getValue(ShowChats.class);
                        if (showChats!=null && isChat){
                            holder.lastMessage.setText(showChats.getLastMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        if (isChat){
//            if (user.getStatus().equals("online")){
//                holder.img_on.setVisibility(View.VISIBLE);
//                holder.img_off.setVisibility(View.GONE);
//            }else{
//                holder.img_on.setVisibility(View.GONE);
//                holder.img_off.setVisibility(View.VISIBLE);
//            }
//        }else{
//            holder.img_off.setVisibility(View.GONE);
//            holder.img_on.setVisibility(View.GONE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("userId",user.getuId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView lastMessage;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            lastMessage=itemView.findViewById(R.id.last_msg);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
        }
    }
}