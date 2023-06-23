package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class ReviewListData {

    @SerializedName("Review_PK")
    private int Review_PK;
    @SerializedName("User_FK")
    private int User_FK;
    @SerializedName("RPT_User_FK")
    private int RPT_User_FK;
    @SerializedName("Text_Con")
    private String Text_Con;
    @SerializedName("Check_Box")
    private int Check_Box;
    @SerializedName("TS")
    private String TS;
    @SerializedName("User_NM")
    private String User_NM;
    @SerializedName("User_Img")
    private byte[] User_Img;

    public ReviewListData(int Review_PK,int User_FK, int RPT_User_FK, String Text_Con, int Check_Box, String TS,String User_NM,byte[] User_Img) {
        Review_PK = this.Review_PK;
        User_FK = this.User_FK;
        RPT_User_FK = this.RPT_User_FK;
        Text_Con = this.Text_Con;
        Check_Box = this.Check_Box;
        TS = this.TS;
        User_NM = this.User_NM;
        User_Img = this.User_Img;
    }



    public int getReview_PK() {
        return Review_PK;
    }
    public void setReview_PK(int review_PK) {
        Review_PK = review_PK;
    }

    public int getUser_FK() {
        return User_FK;
    }
    public void setUser_FK(int user_FK) {
        User_FK = user_FK;
    }

    public int getRPT_User_FK() {
        return RPT_User_FK;
    }
    public void setRPT_User_FK(int RPT_User_FK) {
        this.RPT_User_FK = RPT_User_FK;
    }

    public String getText_Con() {
        return Text_Con;
    }
    public void setText_Con(String text_Con) {
        Text_Con = text_Con;
    }

    public int getCheck_Box() {
        return Check_Box;
    }
    public void setCheck_Box(int check_Box) {
        Check_Box = check_Box;
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
