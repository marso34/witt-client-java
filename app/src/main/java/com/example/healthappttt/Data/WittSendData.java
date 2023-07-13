package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class WittSendData {

    @SerializedName("user_PK")
    int user_PK;
    @SerializedName("otherUserPK")
    int otherUserPK;
    @SerializedName("user_NM")
    String user_NM;
    @SerializedName("otherUserNM")
    String otherUserNM;

    public WittSendData(int user_PK, int otherUserPK, String user_NM, String otherUserNM) {
        this.user_PK = user_PK;
        this.otherUserPK = otherUserPK;
        this.user_NM = user_NM;
        this.otherUserNM = otherUserNM;
    }

    public int getUser_PK() {
        return user_PK;
    }
    public void setUser_PK(int user_PK) {
        this.user_PK = user_PK;
    }

    public int getOtherUserPK() {
        return otherUserPK;
    }
    public void setOtherUserPK(int otherUserPK) {
        this.otherUserPK = otherUserPK;
    }

    public String getUser_NM() {
        return user_NM;
    }
    public void setUser_NM(String user_NM) {
        this.user_NM = user_NM;
    }

    public String getOtherUserNM() {
        return otherUserNM;
    }
    public void setOtherUserNM(String otherUserNM) {
        this.otherUserNM = otherUserNM;
    }
}
