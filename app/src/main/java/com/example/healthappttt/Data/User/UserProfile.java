package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    public UserProfile(String NM) {
        this.User_NM = NM;
    }
    // 서버에서 제공하는 형식
    /*USER_PK: TB.USER_PK,
      EMAIL: TB.EMAIL,
      IP: TB.IP,
      Platform: TB.Platform,
      PW: TB.PW,
      User_Img: TB.User_Img,
      User_NM: TB.User_NM*/

    @SerializedName("USER_PK")
    private int USER_PK;

    @SerializedName("EMAIL")
    private String EMAIL;

    @SerializedName("IP")
    private int IP;

    @SerializedName("Platform")
    private int Platform;

    @SerializedName("User_NM")
    private String User_NM;

    @SerializedName("User_Img")
    private byte[] User_Img;

    @SerializedName("PW")
    private  String PW;

    @SerializedName("location")
    private  String location;

    public int getUSER_PK() {
        return USER_PK;
    }

    public void setUSER_PK(int USER_PK) {
        this.USER_PK = USER_PK;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEmail(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public int getIP() {
        return IP;
    }

    public void setIP(int IP) {
        this.IP = IP;
    }

    public int getPlatform() {
        return Platform;
    }

    public void setPlatform(int platform) {
        this.Platform = platform;
    }

    public String getUser_NM() {
        return User_NM;
    }

    public void setUser_NM(String user_NM) {
        this.User_NM = user_NM;
    }

    public byte[] getUser_Img() {
        return User_Img;
    }

    public void setUser_Img(byte[] user_Img) {
        this.User_Img = user_Img;
    }

    public String getPW() { return PW;}

    public void setPW(String PW) { this.PW = PW;}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
