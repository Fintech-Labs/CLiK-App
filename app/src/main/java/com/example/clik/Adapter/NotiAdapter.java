package com.example.clik.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Fragements.ProfileFragment;
import com.example.clik.Model.Noti;
import com.example.clik.Model.User;
import com.example.clik.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder>{

    private Context context;
    private List<Noti> noti;

    public NotiAdapter(Context context, List<Noti> noti) {
        this.context = context;
        this.noti = noti;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noti_item,parent,false);
        return new NotiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Noti notif = noti.get(position);
        holder.comment.setText(notif.getText());
        getUserInfo(holder.image_profile,holder.fullname,notif.getUserid());

        if(notif.isIspost()){
            holder.post_image.setVisibility(View.VISIBLE);
        }
        else {
            holder.post_image.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notif.isIspost()){
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid",notif.getPostid());
                    editor.apply();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noti.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,post_image;
        public TextView fullname , comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile =itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            fullname  =itemView.findViewById(R.id.full_name);

            comment =itemView.findViewById(R.id.noti);
        }
    }
    private void getUserInfo(final ImageView imageView, final TextView full_name, String publisherid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(publisherid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getProfileUri()).into(imageView);
                full_name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
