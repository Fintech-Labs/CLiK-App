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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Adapter.PhotoAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private DatabaseReference ref1;

    private List<Post> myPhotoList;
    private PhotoAdapter photoAdpatar;
    private RecyclerView recyclerView;

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

        myPhotoList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        photoAdpatar = new PhotoAdapter(getContext(), myPhotoList, firebaseUser.getUid());
        recyclerView.setAdapter(photoAdpatar);

        ref1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid());

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), more);

                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile2Activity.class));
            }
        });

        getUserData();
        setNoOfFollowers(noOffollowers);
        setNoOfFollowing(noOffollowing);
        getPostData(noOfPosts);

        return v;
    }

    private void getPostData(final TextView noOfPosts) {
        DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference().child("posts");
        ref5.keepSynced(true);

        ref5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getPublisher().equals(firebaseUser.getUid())) {
                        myPhotoList.add(post);
                    }
                }
                noOfPosts.setText(String.valueOf(myPhotoList.size()));
                Collections.reverse(myPhotoList);
                photoAdpatar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNoOfFollowing(final TextView noOffollowing) {
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
    }

    private void setNoOfFollowers(final TextView noOffollowers) {
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
    }

    private void getUserData() {
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
                username.setText(user.getName());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}