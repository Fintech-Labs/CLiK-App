package com.example.clik.Model;

public class Post {

    private String discription;
    private String postId;
    private String publisher;
    private String ImageUri;

    public Post() {
    }

    public Post(String discription, String postId, String publisher, String imageUri) {
        this.discription = discription;
        this.postId = postId;
        this.publisher = publisher;
        ImageUri = imageUri;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}
