package com.example.clik.Model;

public class Post {

    private String discription;
    private String postId;
    private String publisher;

    public Post() {
    }

    public Post(String discription, String postId, String publisher) {
        this.discription = discription;
        this.postId = postId;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
