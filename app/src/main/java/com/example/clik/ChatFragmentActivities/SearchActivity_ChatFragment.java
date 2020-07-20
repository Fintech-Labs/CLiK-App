package com.example.clik.ChatFragmentActivities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Model.ShowChats;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity_ChatFragment extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;

    private UserAdapterChat userAdapterChat;
    private List<User> userList;
    private List<ShowChats> showChatsList;

    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__chat_fragment);

        toolbar=findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity_ChatFragment.this));

        userList=new ArrayList<>();
        showChatsList=new ArrayList<>();

        readUsers();

        //Search Text Code
        searchText=findViewById(R.id.searchtext);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void searchUsers(String s){
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("Name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot2 :snapshot.getChildren()){
                    User user = snapshot2.getValue(User.class);
                    userList.add(user);
                }
                userAdapterChat.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity_ChatFragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readUsers(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getuId().equals(firebaseUser.getUid())){
                        userList.add(user);
                    }
                }

                userAdapterChat=new UserAdapterChat(SearchActivity_ChatFragment.this,userList,true);
                recyclerView.setAdapter(userAdapterChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}