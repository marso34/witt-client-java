package com.example.healthappttt.Data.Chat;

public class MSG {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 0;
    private int myFlag;
    private int chatRoomId;
    private String message;
    private String timestamp;

    public MSG(int myFlag, int chatRoomId, String message, String timestamp) {
        this.myFlag = myFlag;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return Long.parseLong(timestamp);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public int getMyFlag() {
        return myFlag;
    }
}
