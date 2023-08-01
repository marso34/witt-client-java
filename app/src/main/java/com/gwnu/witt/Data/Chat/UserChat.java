package com.gwnu.witt.Data.Chat;

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
    @SerializedName("GYM_NM")
    private String GNM;
    @SerializedName("GYM_Adress")
    private String GADS;
    @SerializedName("FAV")
    private int FAV;

    public UserChat(String userNM, int otherUserKey, int chatRoomId, String lastChat, String lastChatTime, String TS,String GNM,String GADS) {
        this.userNM = userNM;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
        this.TS = TS;
        this.GNM = GNM;
        this.GADS =GADS;
    }

    public UserChat(String other_user_nm,int otherUserKey, int chatRoomId, String TS,String GNM,String GADS,int FAV) {
        this.userNM = other_user_nm;
        this.otherUserKey = otherUserKey;
        this.chatRoomId = chatRoomId;
        this.lastChat = "";
        this.lastChatTime = "";
        this.TS = TS;
        this.GNM = GNM;
        this.GADS =GADS;
        this.FAV = FAV;

    }
    public int getFAV(){ return FAV; }
    public String getGNM (){ return GNM; }
    public String getGADS (){ return GADS; }
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
