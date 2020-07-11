package com.example.clik.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Model.Comment;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdpater  extends RecyclerView.Adapter<CommentAdpater.ViewHolder>{

    private Context mContext;
    private List<Comment> mComments;

    public CommentAdpater(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Comment comment = mComments.get(position);

        holder.comment.setText(comment.getComment());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(comment.getPublisher());
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user = snapshot.getValue(User.class);
                assert user != null;
                Picasso.get().load(user.getProfileUri()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profile_pic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(user.getProfileUri()).into(holder.profile_pic);
                    }
                });

                holder.userName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_pic;
        public TextView comment;
        private TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_pic);
            comment = itemView.findViewById(R.id.comment);
            userName = itemView.findViewById(R.id.username);
        }
    }
}
