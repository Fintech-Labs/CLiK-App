package com.example.clik.ChatFragmentActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.clik.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity_ChatFragment extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;

    private UserAdapterChat userAdapterChat;
    private List<UserChat> userChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__chat_fragment);

        toolbar=findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity_ChatFragment.this));

        userChatList=new ArrayList<>();

    }

    private void readUsers(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userChatList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserChat userChat=dataSnapshot.getValue(UserChat.class);

                    assert userChat != null;
                    assert firebaseUser != null;
                    if (!userChat.getId().equals(firebaseUser.getUid())){
                        userChatList.add(userChat);
                    }
                }

                userAdapterChat=new UserAdapterChat(SearchActivity_ChatFragment.this,userChatList);
                recyclerView.setAdapter(userAdapterChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}