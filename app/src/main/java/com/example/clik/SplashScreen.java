package com.example.clik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clik.ChatFragmentActivities.CommonFunctions;

public class SplashScreen extends AppCompatActivity {

    CommonFunctions commonFunctions=new CommonFunctions(SplashScreen.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, WelcomeActivity.class));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        finish();

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
        }
    }
}
