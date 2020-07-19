package com.example.clik.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Model.Comment;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.example.clik.userprofile.ProfileActivity;
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

import java.util.List;
import java.util.Objects;

public class CommentAdpater  extends RecyclerView.Adapter<CommentAdpater.ViewHolder>{

    private Context mContext;
    private List<Comment> mComments;
    private String postId;

    private FirebaseUser fuser;
    private AlertDialog.Builder ad;

    public CommentAdpater(Context mContext, List<Comment> mComments, String postId) {
        this.mContext = mContext;
        this.mComments = mComments;
        this.postId = postId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Comment comment = mComments.get(position);

        holder.comment.setText(comment.getComment());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(comment.getPublisher());
        ref.keepSynced(true);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

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

        holder.comment_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (comment.getPublisher().equals(fuser.getUid())){
                    PopupMenu popup = new PopupMenu(Objects.requireNonNull(mContext), holder.comment_layout);

                    popup.getMenuInflater().inflate(R.menu.current_user_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete:
                                    ad = new AlertDialog.Builder(mContext);
                                    ad.setTitle("Delete");
                                    ad.setMessage("Are You Sure To Delete The Comment");
                                    ad.setCancelable(true);

                                    ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseDatabase.getInstance().getReference().child("comments").child(postId).child(comment.getCommentId()).removeValue();
                                            Toast.makeText(mContext, "Comment Removed Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alert11 = ad.create();
                                    alert11.show();

                            }
                            return true;
                        }
                    });

                    popup.show();
                } else {
                    PopupMenu popup = new PopupMenu(Objects.requireNonNull(mContext), holder.comment_layout);

                    popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.redirect:
                                    Intent intent = new Intent(mContext, ProfileActivity.class);
                                    intent.putExtra("publisherId", comment.getPublisher());
                                    mContext.startActivity(intent);
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
                return true;
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

        private RelativeLayout comment_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_pic);
            comment = itemView.findViewById(R.id.comment);
            userName = itemView.findViewById(R.id.username);

            comment_layout = itemView.findViewById(R.id.comment_layout);
        }
    }
}
