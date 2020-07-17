package com.example.clik.ChatFragmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.clik.R;
import com.google.android.material.appbar.MaterialToolbar;

public class SearchActivity_ChatFragment extends AppCompatActivity {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__chat_fragment);

        toolbar=(MaterialToolbar)findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}