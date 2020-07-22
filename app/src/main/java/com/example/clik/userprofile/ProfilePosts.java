package com.example.clik.userprofile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Adapter.PostAdapter;
import com.example.clik.ChatFragmentActivities.CommonFunctions;
import com.example.clik.Model.Post;
import com.example.clik.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfilePosts extends AppCompatActivity {

    private String publisherId;
    private List<Post> mPosts;
    private RecyclerView profile_posts;
    private PostAdapter postAdapter;

    CommonFunctions commonFunctions=new CommonFunctions(ProfilePosts.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_posts);

        publisherId = getIntent().getStringExtra("publisherId");
        mPosts = new ArrayList<>();

        profile_posts = findViewById(R.id.profile_posts);

        profile_posts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfilePosts.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        profile_posts.setLayoutManager(linearLayoutManager);

        createPostList();
    }

    private void createPostList() {
        final ProgressDialog pd = new ProgressDialog(ProfilePosts.this);
        pd.setMessage("Loading");
        pd.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Post post = snapshot1.getValue(Post.class);
                    assert post != null;
                    if(post.getPublisher().equals(publisherId)){
                        mPosts.add(post);
                    }
                }
                postAdapter = new PostAdapter(ProfilePosts.this, mPosts);
                profile_posts.setAdapter(postAdapter);
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePosts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((commonFunctions.getCurrentUser())!=null){
            (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                    .child("status").setValue("true");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((commonFunctions.getCurrentUser())!=null){
            (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                    .child("status").setValue("false");
        }
    }
}