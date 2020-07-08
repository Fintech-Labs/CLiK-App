package com.example.clik.Model;

public class User {
    String Bio;
    String Email;
    String Name;
    String Phonenumber;

    public User() {
    }

    public User(String bio, String email, String name, String phonenumber) {
        Bio = bio;
        Email = email;
        Name = name;
        Phonenumber = phonenumber;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }
}
