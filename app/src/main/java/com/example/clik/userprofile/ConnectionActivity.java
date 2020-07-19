package com.example.clik.userprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.clik.R;
import com.example.clik.userprofile.Fragements.FollowersFragment;
import com.example.clik.userprofile.Fragements.FollowingFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ConnectionActivity extends AppCompatActivity {

    private String Uid;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FollowersFragment followersFragment;
    private FollowingFragment followingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Uid = getIntent().getStringExtra("Uid");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        followersFragment = new FollowersFragment(Uid);
        followingFragment = new FollowingFragment(Uid);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdaptor viewPagerAdpater = new ViewPagerAdaptor(getSupportFragmentManager(), 0);
        viewPagerAdpater.addFragment(followersFragment, "Followers");
        viewPagerAdpater.addFragment(followingFragment, "Following");
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