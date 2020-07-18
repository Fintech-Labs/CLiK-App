package com.example.clik.ChatFragmentActivities;

public class Chat {
    public String sender;
    public String receiver;
    public String message;

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Chat(){}

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
