package com.example.clik;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clik.Fragements.ChatFragment;
import com.example.clik.Fragements.FeedFragment;
import com.example.clik.Fragements.FriendsFragment;
import com.example.clik.Fragements.NotiFragment;
import com.example.clik.Fragements.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment sFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new FeedFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_feed:
                            sFragment = new FeedFragment();
                            break;

                        case R.id.nav_chat:
                            sFragment = new ChatFragment();
                            break;

                        case R.id.nav_search:
                            sFragment = new NotiFragment();
                            break;

                        case R.id.nav_settings:
                            sFragment = new ProfileFragment();
                            break;

                    }

                    if (sFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, sFragment).commit();
                    }

                    return true;
                }
            };
}