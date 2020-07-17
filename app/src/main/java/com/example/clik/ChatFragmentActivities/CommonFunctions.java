package com.example.clik.ChatFragmentActivities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CommonFunctions {
    Context context;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public CommonFunctions(Context context) {
        this.context = context;
    }

    public String getUid(){
        return firebaseAuth.getUid();
    }

    public String getUserName(){
        final String[] username = {""};

        databaseReference.child("users").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username[0] = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return username[0];
    }
}
