package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class ReportHistory {

    @SerializedName("RPT_CAT_FK")
    private int RPT_CAT_FK; //카테고리
    @SerializedName("USER_NM")
    private String USER_NM; //신고한 사람 이름
    @SerializedName("CONT")
    private String CONT; //신고 내용
    @SerializedName("TS")
    private String TS;
    @SerializedName("GYM_NM")
    private String GYM_NM;

    public ReportHistory(int RPT_CAT_FK, String USER_NM, String CONT, String TS, String GYM_NM) {
        this.RPT_CAT_FK = RPT_CAT_FK;
        this.USER_NM = USER_NM;
        this.CONT = CONT;
        this.TS = TS;
        this.GYM_NM = GYM_NM;
    }


    public int getRPT_CAT_FK() {
        return RPT_CAT_FK;
    }
    public void setRPT_CAT_FK(int RPT_CAT_FK) {
        this.RPT_CAT_FK = RPT_CAT_FK;
    }

    public String getUser_NM() {
        return USER_NM;
    }
    public void setUser_NM(String user_NM) {
        this.USER_NM = user_NM;
    }

    public String getCONT() {
        return CONT;
    }
    public void setCONT(String CONT) {
        this.CONT = CONT;
    }

    public String getTS() {
        return TS;
    }
    public void setTS(String TS) {
        this.TS = TS;
    }

    public String getGYM_NM() {
        return GYM_NM;
    }
    public void setGYM_NM(String GYM_NM) {
        this.GYM_NM = GYM_NM;
    }
}
