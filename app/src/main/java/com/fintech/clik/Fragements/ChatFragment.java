package com.fintech.clik.Fragements;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fintech.clik.ChatFragmentActivities.SearchActivity_ChatFragment;
import com.fintech.clik.ChatFragmentActivities.UserAdapterChat;
import com.fintech.clik.Model.Chat;
import com.fintech.clik.Model.ShowChats;
import com.fintech.clik.Model.User;
import com.fintech.clik.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    FloatingActionButton searchBtn;
    private RecyclerView recyclerView;

    private UserAdapterChat userAdapterChat;
    private List<User> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    private List<String> usersList;
    private List<ShowChats> showChatsList;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        searchBtn = view.findViewById(R.id.chatSearchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity_ChatFragment.class));
            }
        });

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        showChatsList=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("ChatUsers").child(mAuth.getUid());
        createMyUserList();

        return view;
    }

    private void createMyUserList(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShowChats showChats = snapshot.getValue(ShowChats.class);

//                    if (chat.getSender().equals(firebaseUser.getUid())) {
//                        usersList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(firebaseUser.getUid())) {
//                        usersList.add(chat.getSender());
//                    }
                    usersList.add(snapshot.getKey());
                    showChatsList.add(showChats);
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender() != null) {
                        if (chat.getSender().equals(firebaseUser.getUid())) {
                            usersList.add(chat.getReceiver());
                        }
                        if (chat.getReceiver().equals(firebaseUser.getUid())) {
                            usersList.add(chat.getSender());
                        }
                    }

                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    // Display unique users from chats
                    for (String id : usersList) {
                        assert user != null;
                        if (user.getuId().equals(id)) {
                            if (!mUsers.isEmpty()) {
                                int i=0;
                                for (i=0;i<mUsers.size();i++) {
                                    User user1=mUsers.get(i);
                                    if (user.getuId().equals(user1.getuId())) {
//                                        Log.i("Check UIDs",user.getuId()+", "+user1.getuId());
                                        break;
                                    }
                                }
                                if (i==mUsers.size()) {
                                    mUsers.add(user);

                                }
                            } else {
                                mUsers.add(user);
                            }
                        }
                    }

                }



                userAdapterChat = new UserAdapterChat(getContext(), mUsers,true);
                recyclerView.setAdapter(userAdapterChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}