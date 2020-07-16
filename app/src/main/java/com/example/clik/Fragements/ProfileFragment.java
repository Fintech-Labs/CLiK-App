package com.example.clik.Fragements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.clik.Model.Post;
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
    private TextView posts,followers,following;

    FirebaseUser firebaseUser;
    String profileid;


    ImageButton myphotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        profileid = prefs.getString("profileid","none");

        profile_pic = v.findViewById(R.id.profile_pic);
        fullName = v.findViewById(R.id.full_name);
        bio = v.findViewById(R.id.bio);
        editProfile = v.findViewById(R.id.editprofile);
        posts = v.findViewById(R.id.no_posts);
        followers = v.findViewById(R.id.no_followers);
        following = v.findViewById(R.id.no_following);

        userInfo();
        getFollowers();
        getpostsno();

        if(profileid.equals(firebaseUser.getUid())){
            editProfile.setText("edit profile");
        } else{
            checkFollow();

        }


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = editProfile.getText().toString();

                if(btn.equals("edit profile")){
                    //go to edit profile
                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                }else if(btn.equals("following")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("folllowers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        return v;
    }
    private void userInfo(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(profileid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext() == null){
                    return;
                }

                User user = snapshot.getValue(User.class);

                Picasso.get().load(user.getProfileUri()).into(profile_pic);
                fullName.setText(user.getName());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkFollow(){
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileid).exists()){
                    editProfile.setText("following");
                }else {
                    editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getFollowers(){
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference2 =FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getpostsno(){
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Post post = snapshot1.getValue(Post.class);
                    if(post.getPublisher().equals(profileid)){
                        ++i;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}