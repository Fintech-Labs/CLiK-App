package com.example.clik.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.example.clik.Feed.CommentActivity;
import com.example.clik.Listner.IFirebaseLoadDone;
import com.example.clik.Model.Post;
import com.example.clik.Model.PostImages;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.example.clik.Transformer.DepthPageTransformer;
import com.example.clik.userprofile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    private List<PostImages> mImages;
    private PhotoAdapter photoAdapter;

    private FirebaseUser fuser;
    private IFirebaseLoadDone iFirebaseLoadDone;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPosts.get(position);
        isLiked(post.getPostId(), holder.like);
        noLikes(post.getPostId(), holder.noOfLikes);
        getComments(post.getPostId(), holder.noOfComments);

        holder.description.setText(post.getDiscription());

        mImages = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(post.getPublisher());
        ref.keepSynced(true);

        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("wait");
        pd.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user = snapshot.getValue(User.class);
                assert user != null;
                holder.username.setText(user.getName());
                holder.bio.setText(user.getBio());
                Picasso.get().load(user.getProfileUri()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(user.getProfileUri()).into(holder.imageProfile);
                    }
                });
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("postImages").child(post.getPostId());
        ref2.keepSynced(true);

        holder.viewPager.setPageTransformer(true, new DepthPageTransformer());

        photoAdapter = new PhotoAdapter(mContext,  mImages);
        holder.viewPager.setAdapter(photoAdapter);

        final ProgressDialog pd2 = new ProgressDialog(mContext);
        pd2.setMessage("wait");
        pd2.show();

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImages.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    PostImages postImages = snapshot1.getValue(PostImages.class);
                    mImages.add(postImages);
                    photoAdapter.notifyDataSetChanged();
                }


                pd2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("likes").
                            child(post.getPostId()).child(fuser.getUid()).child("uId").setValue(fuser.getUid());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("likes").
                            child(post.getPostId()).child(fuser.getUid()).removeValue();
                }

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);

            }
        });


        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("publisherId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("publisherId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.doubleTapLikeView.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
            @Override
            public void onDoubleTap(View view) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("likes").
                            child(post.getPostId()).child(fuser.getUid()).child("uId").setValue(fuser.getUid());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("likes").
                            child(post.getPostId()).child(fuser.getUid()).removeValue();
                }
            }

            @Override
            public void onTap() {

            }
        });

    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private void noLikes(String postId, final TextView noOfLikes) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noOfLikes.setText(dataSnapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, final TextView noOfComments) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).
                addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        noOfComments.setText(dataSnapshot.getChildrenCount() + " ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(fuser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart_red);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart2);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).keepSynced(true);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView like;
        public ImageView comment;
        public ImageView more;

        public TextView username;
        public TextView bio;
        public TextView noOfLikes;
        public TextView noOfComments;
        SocialTextView description;

        private ViewPager viewPager;
        private DoubleTapLikeView doubleTapLikeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            doubleTapLikeView = itemView.findViewById(R.id.layout_double_tap_like);
            viewPager = itemView.findViewById(R.id.viewPager);
            imageProfile = itemView.findViewById(R.id.profile_pic);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.username);
            bio = itemView.findViewById(R.id.bio);
            noOfLikes = itemView.findViewById(R.id.no_likes);
            noOfComments = itemView.findViewById(R.id.no_comments);
            description = itemView.findViewById(R.id.post_description);


        }
    }
}
