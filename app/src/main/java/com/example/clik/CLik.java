package com.example.clik;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.clik.ChatFragmentActivities.CommonFunctions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class CLik extends Application {

    CommonFunctions commonFunctions;
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        commonFunctions=new CommonFunctions(CLik.this);

        (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot!=null) {
                            (commonFunctions.getReference()).child("users").child(commonFunctions.getUid())
                                    .child("status").onDisconnect().setValue("false");

//                            (commonFunctions.getReference()).child("lastSeen").child(commonFunctions.getUid())
//                                    .onDisconnect().setValue(ServerValue.TIMESTAMP);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
