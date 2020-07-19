package com.example.clik.Feed.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Adapter.CommentAdpater;
import com.example.clik.Model.Comment;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CommentFragment extends Fragment {

    private String postId;

    private ImageView profile_pic;
    private EditText comment;
    private TextView post;

    private FirebaseUser fuser;
    private DatabaseReference ref;

    private List<Comment> mComments;
    private RecyclerView recyclerView;
    private CommentAdpater commentAdpater;

    public CommentFragment(String postId) {
        this.postId = postId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment, container, false);

        profile_pic = v.findViewById(R.id.profile_pic);
        comment = v.findViewById(R.id.comment_text);
        post = v.findViewById(R.id.post);

        mComments = new ArrayList<>();

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
        ref1.keepSynced(true);

        ref1.addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Adding Comment");
                pd.show();
                String comment_text = comment.getText().toString().trim();

                if(comment_text.isEmpty()){
                    comment.setError("Empty");
                    comment.requestFocus();
                    pd.dismiss();
                }
                else{
                    ref = FirebaseDatabase.getInstance().getReference().child("comments").child(postId);

                    String commentId = ref.push().getKey();

                    HashMap<String , Object> map = new HashMap<>();
                    map.put("publisher", fuser.getUid());
                    map.put("comment", comment_text);
                    map.put("commentId", commentId);

                    assert commentId != null;
                    ref.child(commentId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                                comment.getText().clear();
                                pd.dismiss();
                            }
                            else{
                                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
                }
            }
        });

        recyclerView = v.findViewById(R.id.comment_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        loadComments();

        return v;
    }

    private void loadComments() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading Comments");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mComments.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Comment comment = snapshot1.getValue(Comment.class);
                    mComments.add(comment);
                }

                commentAdpater = new CommentAdpater(getContext(), mComments, postId);
                recyclerView.setAdapter(commentAdpater);
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }
}