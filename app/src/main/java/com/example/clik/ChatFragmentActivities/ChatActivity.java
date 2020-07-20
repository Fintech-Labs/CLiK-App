package com.example.clik.ChatFragmentActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clik.Model.Chat;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    CommonFunctions commonFunctions;

    CircleImageView profile_image;
    TextView username;
    RecyclerView recyclerView;

    ImageButton btn_send,videoBtn;
    EditText text_send;

    MessageAdapterChat messageAdapterChat;
    List<Chat> chatList;

    Intent intent;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        commonFunctions=new CommonFunctions(ChatActivity.this);

        toolbar=findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        videoBtn=findViewById(R.id.videoBtn);
        text_send=findViewById(R.id.text_send);
        recyclerView=findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent=getIntent();
        final String userId=intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users").child(userId);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=text_send.getText().toString();

                if (!TextUtils.isEmpty(msg)){
                    sendMessage(firebaseUser.getUid(),userId,msg);
                }else{
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText(null);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                username.setText(user.getName());
                Picasso.get().load(user.getProfileUri()).into(profile_image);

                readMessages(firebaseUser.getUid(),userId,user.getProfileUri());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        seenMessage(userId);


        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("Start Video Call?")
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ChatActivity.this, "Starting video call!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    private void seenMessage(String userid){
        reference=FirebaseDatabase.getInstance().getReference("Chats").child(getChatRoomId(userid,firebaseUser.getUid()));
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("isSeen",true);
                    snapshot.getRef().updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isSeen",false);
        hashMap.put("time", ServerValue.TIMESTAMP);

        reference.child("Chats").child(getChatRoomId(sender,receiver)).push().setValue(hashMap);

        hashMap.clear();
        hashMap.put("lastMessageBy",sender);
        hashMap.put("lastMessage",message);

        reference.child("ChatUsers").child(sender).child(receiver).setValue(hashMap);
        reference.child("ChatUsers").child(receiver).child(sender).setValue(hashMap);

    }

    String getChatRoomId(String a, String b) {
        if (a.compareTo(b)<0) {
            return b+"_"+a;
        } else {
            return a+"_"+b;
        }
    }

    private void readMessages(final String myId, final String userId, final String imageUrl){
        chatList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats").child(getChatRoomId(myId,userId));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
//
//                    if ((chat.getReceiver().equals(myId) && chat.getSender().equals(userId)) ||
//                            (chat.getSender().equals(myId) && chat.getReceiver().equals(userId))){
//                        chatList.add(chat);
//                    }

                    chatList.add(chat);

                    messageAdapterChat=new MessageAdapterChat(ChatActivity.this,chatList,imageUrl);
                    recyclerView.setAdapter(messageAdapterChat);
                    recyclerView.scrollToPosition(chatList.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void status(String status){
//        reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
//
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("status",status);
//
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        reference.removeEventListener(seenListener);
//        status("offline");
//    }
}