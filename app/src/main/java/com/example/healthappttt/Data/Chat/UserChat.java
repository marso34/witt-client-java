package com.example.healthappttt.Data.Chat;

import com.google.gson.annotations.SerializedName;

public class UserChat {
    @SerializedName("userNM")
    private String userNM;

    @SerializedName("otherUserKey")
    private String otherUserKey;

    @SerializedName("chatRoomId")
    private String chatRoomId;

    @SerializedName("lastChat")
    private String lastChat;

    @SerializedName("lastChatTime")
    private String lastChatTime;

    @SerializedName("timeStamp")
    private String TS;

    public UserChat(String userNM, String otherUserKey, String chatRoomId, String lastChat, String lastChatTime,String TS) {
        this.userNM = userNM;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
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

    public String getOtherUserKey() {
        return otherUserKey;
    }

    public void setOtherUserKey(String otherUserKey) {
        this.otherUserKey = otherUserKey;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
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
