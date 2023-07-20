package com.example.healthappttt.Data.Chat;

import com.google.gson.annotations.SerializedName;

public class getMSGKey {

    @SerializedName("ID")
    private int userKey;
    @SerializedName("chatRoomId")
    private int chatRoomId;

    public  getMSGKey(int userKey,int chatRoomId){
        this.userKey = userKey;
        this.chatRoomId = chatRoomId;
    }
}
