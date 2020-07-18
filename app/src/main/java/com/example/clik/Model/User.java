package com.example.clik.Model;

public class User {
    String uId;
    String Bio;
    String Name;
    String ProfileUri;


    public User() {
    }

    public User(String uId, String bio, String name, String profileUri) {
        this.uId = uId;
        this.Bio = bio;
        this.Name = name;
        this.ProfileUri = profileUri;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileUri() {
        return ProfileUri;
    }

    public void setProfileUri(String profileUri) {
        ProfileUri = profileUri;
    }
}
