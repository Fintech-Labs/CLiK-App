package com.example.clik.Fragements;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clik.ChatFragmentActivities.ChatActivity;
import com.example.clik.ChatFragmentActivities.SearchActivity_ChatFragment;
import com.example.clik.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatFragment extends Fragment {

    FloatingActionButton searchBtn;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_chat, container, false);

        searchBtn=view.findViewById(R.id.chatSearchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ChatActivity.class));
            }
        });

        return view;
    }
}