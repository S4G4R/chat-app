package com.example.chatapp.chats;

import com.google.firebase.auth.FirebaseAuth;

// POJO for a Chat.

public class Chats {

    private String timeStamp;
    private String receiverId;
    private String senderId;
    private String message;

    public Chats() {}

    public Chats(String timeStamp, String receiverId, String senderId, String message) {
        this.timeStamp  =  timeStamp;
        this.receiverId =  receiverId;
        this.senderId   =  senderId;
        this.message    =  message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSender() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid().equals(getSenderId());
    }

}
