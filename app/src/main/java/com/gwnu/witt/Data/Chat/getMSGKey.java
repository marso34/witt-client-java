package com.gwnu.witt.Data.Chat;

import com.google.gson.annotations.SerializedName;

public class getMSGKey {

    @SerializedName("ID")
    private int userKey;
    @SerializedName("chatRoomId")
    private int chatRoomId;
    @SerializedName("chatPk")
    private int chatPk;
    @SerializedName("timeStamp")
    private String timeStamp;

    public  getMSGKey(int userKey,int chatRoomId,int chatPk,String timeStamp){
        this.userKey = userKey;
        this.chatRoomId = chatRoomId;
        this.chatPk = chatPk;
        this.timeStamp = timeStamp;
    }
}
