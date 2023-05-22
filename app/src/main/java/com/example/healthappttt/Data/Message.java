package com.example.healthappttt.Data;

public class Message {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;

    private String username;
    private String message;
    private long timestamp;

    public Message(String username, String message, long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}