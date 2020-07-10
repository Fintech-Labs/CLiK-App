package com.example.clik.Model;

public class Post {

    private String discription;
    private String postId;
    private String postImageUri;
    private String publisher;

    public Post() {
    }

    public Post(String discription, String postId, String postImageUri, String publisher) {
        this.discription = discription;
        this.postId = postId;
        this.postImageUri = postImageUri;
        this.publisher = publisher;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImageUri() {
        return postImageUri;
    }

    public void setPostImageUri(String postImageUri) {
        this.postImageUri = postImageUri;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
