package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class WittListData {

    @SerializedName("RECORD_PK")
    private int RECORD_PK;
    @SerializedName("USER_FK")
    private int USER_FK;
    @SerializedName("OUser_FK")
    private int OUser_FK;
    @SerializedName("TS")
    private String TS;
    @SerializedName("User_NM")
    private String User_NM;
    @SerializedName("User_Img")
    private byte[] User_Img;

    public WittListData(int RECORD_PK, int USER_FK, int OUser_FK, String TS, String User_NM, byte[] User_Img) {
        this.RECORD_PK = RECORD_PK;
        this.USER_FK = USER_FK;
        this.OUser_FK = OUser_FK;
        this.TS = TS;
        this.User_NM = User_NM;
        this.User_Img = User_Img;
    }

    public int getRECORD_PK() {
        return RECORD_PK;
    }
    public void setRECORD_PK(int RECORD_PK) {
        this.RECORD_PK = RECORD_PK;
    }

    public int getUSER_FK() {
        return USER_FK;
    }
    public void setUSER_FK(int USER_FK) {
        this.USER_FK = USER_FK;
    }

    public int getOUser_FK() {
        return OUser_FK;
    }
    public void setOUser_FK(int OUser_FK) {
        this.OUser_FK = OUser_FK;
    }

    public String getTS() {
        return TS;
    }
    public void setTS(String TS) {
        this.TS = TS;
    }

    public String getUser_NM() {
        return User_NM;
    }
    public void setUser_NM(String user_NM) {
        User_NM = user_NM;
    }

    public byte[] getUser_Img() {
        return User_Img;
    }
    public void setUser_Img(byte[] user_Img) {
        User_Img = user_Img;
    }
}
