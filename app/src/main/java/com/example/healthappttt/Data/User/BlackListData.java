package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class BlackListData {

    @SerializedName("BL_PK")
    private int BL_PK;
    @SerializedName("User_NM")
    private String User_NM;
    @SerializedName("OUser_FK")
    private int OUser_FK;
    @SerializedName("TS")
    private String TS;        //getTS는 String으로 넘김
    @SerializedName("User_Img")
    private String User_Img;

    public BlackListData(int BL_PK, String User_NM, int OUser_FK, String TS, String User_Img) {
        this.BL_PK = BL_PK;
        this.User_NM = User_NM;
        this.OUser_FK = OUser_FK;
        this.TS = TS;
        this.User_Img = User_Img;
    }



    public int getBL_PK() {
        return BL_PK;
    }

    public void setBL_PK(int BL_PK) {
        this.BL_PK = BL_PK;
    }

    public int getOUser_FK() {
        return OUser_FK;
    }

    public void setOUser_FK(int OUser_FK) {
        this.OUser_FK = OUser_FK;
    }

    public String getUser_NM() {
        return User_NM;
    }

    public void setUser_NM(String user_NM) {
        User_NM = user_NM;
    }

    public String getTS() {
        return String.valueOf(TS);
    }

    public void setTS(String TS) {
        this.TS = TS;
    }

    public String getUser_Img() {
        return User_Img;
    }

    public void setUser_Img(String user_Img) {
        User_Img = user_Img;
    }
}
