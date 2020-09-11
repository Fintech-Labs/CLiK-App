package com.fintech.clik.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fintech.clik.Model.Post;
import com.fintech.clik.R;
import com.fintech.clik.userprofile.ProfilePosts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPosts;
    private String publisherId;

    public PhotoAdapter(Context mContext, List<Post> mPosts, String publisherId) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        this.publisherId = publisherId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_layout, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageUri()).placeholder(R.drawable.photo_back).networkPolicy(NetworkPolicy.OFFLINE).into(holder.postImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(post.getImageUri()).placeholder(R.drawable.photo_back).into(holder.postImage);
            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfilePosts.class);
                intent.putExtra("publisherId", publisherId);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
            postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
