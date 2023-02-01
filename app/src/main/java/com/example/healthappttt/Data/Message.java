package com.example.healthappttt.Data;

public class Message {

    private String message = "";
    private String senderId = "";
    private String receiver ="";

    public Message(){}

    public Message(String message,  String senderId, String receiver){
        this.message = message;
        this.senderId = senderId;
        this.receiver = receiver;
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
