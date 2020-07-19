package com.example.clik.Feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.clik.Feed.Fragments.CommentFragment;
import com.example.clik.Feed.Fragments.LikesFragment;
import com.example.clik.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LikeCommentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CommentFragment commentFragment;
    private LikesFragment likesFragment;

    private String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = getIntent().getStringExtra("postId");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        commentFragment = new CommentFragment(postId);
        likesFragment = new LikesFragment(postId);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdaptor viewPagerAdpater = new ViewPagerAdaptor(getSupportFragmentManager(), 0);
        viewPagerAdpater.addFragment(commentFragment, "Comment");
        viewPagerAdpater.addFragment(likesFragment, "Likes");
        viewPager.setAdapter(viewPagerAdpater);
    }

    private static class ViewPagerAdaptor extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmensTitle = new ArrayList<>();

        public ViewPagerAdaptor(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String Title){
            fragments.add(fragment);
            fragmensTitle.add(Title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmensTitle.get(position);
        }
    }
}