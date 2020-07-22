package com.example.clik;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clik.ChatFragmentActivities.CommonFunctions;
import com.example.clik.Fragements.ChatFragment;
import com.example.clik.Fragements.FeedFragment;
import com.example.clik.Fragements.ProfileFragment;
import com.example.clik.Fragements.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment sFragment = null;
    FeedFragment feedFragment;
    ChatFragment chatFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;

    CommonFunctions commonFunctions=new CommonFunctions(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedFragment=new FeedFragment();
        chatFragment=new ChatFragment();
        searchFragment=new SearchFragment();
        profileFragment=new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new FeedFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((commonFunctions.getCurrentUser())!=null){
            (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                    .child("status").setValue("true");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((commonFunctions.getCurrentUser())!=null){
            (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                    .child("status").setValue("false");

//            (commonFunctions.getReference()).child("lastSeen").child(commonFunctions.getUid())
//                    .setValue(ServerValue.TIMESTAMP);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_feed:
                            sFragment = feedFragment;
                            break;

                        case R.id.nav_chat:
                            sFragment = chatFragment;
                            break;

                        case R.id.nav_search:
                            sFragment = searchFragment;
                            break;

                        case R.id.nav_settings:
                            sFragment = profileFragment;
                            break;

                    }

                    if (sFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, sFragment).commit();
                    }

                    return true;
                }
            };

}