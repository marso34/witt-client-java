package com.example.healthappttt.Data;

public class Message {

    private String message = "";
    private String senderId = "";

    public Message(){}

    public Message(String message, String senderId){
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return senderId;
    }

    public void setSender(String senderId) {
        this.senderId = senderId;
    }
}
