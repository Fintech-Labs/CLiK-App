package com.example.clik.Listner;

import com.example.clik.Model.PostImages;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<PostImages> postImages);
    void onFirebaseLoadFailde(String message);

}
