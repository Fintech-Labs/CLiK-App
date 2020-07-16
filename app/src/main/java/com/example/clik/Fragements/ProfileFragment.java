package com.example.clik.Fragements;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.clik.Model.Post;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.example.clik.userAuth.LoginActivity;
import com.example.clik.userprofile.EditProfile2Activity;
import com.example.clik.userprofile.SetEmailActivity;
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

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageView profile_pic;
    private TextView fullName;
    private TextView bio;
    private Button editProfile;

    private TextView noOfPosts;
    private TextView noOffollowers;
    private TextView noOffollowing;

    private TextView username;
    private ImageView more;

    private FirebaseUser firebaseUser;
    private int posts = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_pic = v.findViewById(R.id.profile_pic);
        fullName = v.findViewById(R.id.full_name);
        bio = v.findViewById(R.id.bio);
        editProfile = v.findViewById(R.id.editprofile);

        noOfPosts = v.findViewById(R.id.no_posts);
        noOffollowers = v.findViewById(R.id.no_followers);
        noOffollowing = v.findViewById(R.id.no_following);

        username = v.findViewById(R.id.user_name);
        more = v.findViewById(R.id.profile_menu);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        ref.keepSynced(true);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), more);

                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), LoginActivity.class));

                            case R.id.change_email:
                                startActivity(new Intent(getContext(), SetEmailActivity.class));
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

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
                username.setText(user.getName());
                bio.setText(user.getBio());

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

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid());

        final DatabaseReference ref2 = ref1.child("followers");
        ref2.keepSynced(true);

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOffollowers.setText(snapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final DatabaseReference ref3 = ref1.child("following");
        ref3.keepSynced(true);

        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOffollowing.setText(snapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("posts");
        ref4.keepSynced(true);

        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Post post = snapshot1.getValue(Post.class);
                    assert post != null;
                    if (post.getPublisher().equals(firebaseUser.getUid())) {
                        posts++;
                    }
                }

                noOfPosts.setText(posts + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}