package com.example.healthappttt.Data.Chat;

import com.google.gson.annotations.SerializedName;


public class UserChat {
    @SerializedName("userNM")
    private String userNM;

    @SerializedName("otherUserKey")
    private String otherUserKey;

    @SerializedName("chatRoomId")
    private String chatRoomId;



    public UserChat(String userNM, String otherUserKey, String chatRoomId) {
        this.userNM = userNM;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
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

    public void setOtherKey(String otherKey) {
        this.otherUserKey = otherKey;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
