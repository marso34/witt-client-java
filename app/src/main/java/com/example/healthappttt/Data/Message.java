package com.example.healthappttt.Data;

public class Message {

    private String message = "";
    private String senderId = "";
    private String receiver;

    public Message(){}

    public Message(String message, String receiver, String senderId){
        this.message = message;
        this.receiver = receiver;
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

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

}
