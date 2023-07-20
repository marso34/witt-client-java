package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class ReviewData {
    @SerializedName("USER_FK")
    private int userFK; // 받는 사람

    @SerializedName("RPT_USER_FK")
    private int rptUserFk; // 보낸 사람

    @SerializedName("Text_Con")
    private String text;

    @SerializedName("Check_Box")
    private int check_box;

    public ReviewData(int userFK, int rptUserFk, String text, int check_box) {
        this.userFK = userFK;
        this.rptUserFk = rptUserFk;
        this.text = text;
        this.check_box = check_box;
    }
}
