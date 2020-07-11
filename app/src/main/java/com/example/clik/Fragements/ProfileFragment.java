package com.example.clik.Fragements;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.clik.Model.User;
import com.example.clik.R;
import com.example.clik.userprofile.EditProfile2Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView profile_pic;
    private TextView fullName;
    private TextView bio;
    private Button editProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_pic = v.findViewById(R.id.profile_pic);
        fullName = v.findViewById(R.id.full_name);
        bio = v.findViewById(R.id.bio);
        editProfile = v.findViewById(R.id.editprofile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user = snapshot.getValue(User.class);
                assert user != null;
                Picasso.get().load(user.getProfileUri()).networkPolicy(NetworkPolicy.OFFLINE).into(profile_pic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(user.getProfileUri()).into(profile_pic);
                    }
                });

                fullName.setText(user.getName());
                if(user.getBio()!=null){
                    bio.setText(user.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile2Activity.class));
            }
        });

        return v;
    }
}