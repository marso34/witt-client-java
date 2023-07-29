package com.example.healthappttt.Data.Chat;

import com.google.gson.annotations.SerializedName;

public class UserChat {
    public static final int read = 1;
    public static final int notRead = 2;
    @SerializedName("userNM")
    private String userNM;

    @SerializedName("otherUserKey")
    private int otherUserKey;

    @SerializedName("chatRoomId")
    private int chatRoomId;

    @SerializedName("lastChat")
    private String lastChat;

    @SerializedName("lastChatTime")
    private String lastChatTime;
    @SerializedName("timeStamp")
    private String TS;


    public UserChat(String userNM, int otherUserKey, int chatRoomId, String lastChat, String lastChatTime, String TS) {
        this.userNM = userNM;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
        this.TS = TS;

    }

    public UserChat(String other_user_nm,int otherUserKey, int chatRoomId, String TS) {
        this.userNM = other_user_nm;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
        this.lastChat = "";
        this.lastChatTime = "";
        this.TS = TS;

    }

    public String getTS() {
        return TS;
    }
    public String getUserNM() {
        return userNM;
    }

    public void setUserNM(String userNM) {
        this.userNM = userNM;
    }

    public int getOtherUserKey() {
        return otherUserKey;
    }

    public void setOtherUserKey(int otherUserKey) {
        this.otherUserKey = otherUserKey;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }
}
