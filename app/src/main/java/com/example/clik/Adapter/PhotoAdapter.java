package com.example.clik.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.clik.Model.PostImages;
import com.example.clik.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {

    private Context mContext;
    private List<PostImages> imagesList;
    private LayoutInflater inflater;

    public PhotoAdapter(Context mContext, List<PostImages> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = inflater.inflate(R.layout.photo_item, container, false);

        final ImageView postImage = view.findViewById(R.id.post_image);

        final PostImages postImages = imagesList.get(position);

        Picasso.get().load(postImages.getImageUri()).networkPolicy(NetworkPolicy.OFFLINE).into(postImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(postImages.getImageUri()).into(postImage);
            }
        });

        container.addView(view);
        return view;
    }


}
