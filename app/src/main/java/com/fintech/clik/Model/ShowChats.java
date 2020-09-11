package com.fintech.clik.Model;

public class ShowChats {
    public String lastMessageBy;
    public String lastMessage;

    public ShowChats(String lastMessageBy, String lastMessage) {
        lastMessageBy = lastMessageBy;
        lastMessage = lastMessage;
    }

    public ShowChats(){}

    public String getLastMessageBy() {
        return lastMessageBy;
    }

    public void setLastMessageBy(String lastMessageBy) {
        this.lastMessageBy = lastMessageBy;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
