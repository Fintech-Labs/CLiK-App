package com.example.clik;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clik.Fragements.ChatFragment;
import com.example.clik.Fragements.FeedFragment;
import com.example.clik.Fragements.ProfileFragment;
import com.example.clik.Fragements.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;

    private Toast backToast;
    BottomNavigationView bottomNavigationView;
    Fragment sFragment = null;
    FeedFragment feedFragment;
    ChatFragment chatFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedFragment=new FeedFragment();
        chatFragment=new ChatFragment();
        searchFragment=new SearchFragment();
        profileFragment=new ProfileFragment();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

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

//    public void status(String status){
//        reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
//
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("status",status);
//
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("offline");
//    }

    @Override
    public void onBackPressed() {


        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else{
          backToast =  Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
          backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}