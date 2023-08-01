package com.gwnu.witt.Data.User;

import com.google.gson.annotations.SerializedName;

public class ReportHistory {

    @SerializedName("RPT_CAT_FK")
    private int RPT_CAT_FK; //카테고리
    @SerializedName("USER_NM")
    private String USER_NM; //신고한 사람 이름
    @SerializedName("CONT")
    private int CONT; //신고 내용
    @SerializedName("TS")
    private String TS;
    @SerializedName("GYM_NM")
    private String GYM_NM;

    public ReportHistory(int CONT) {
        this.CONT = CONT;
    }



    public int getCONT() {
        return CONT;
    }
    public void setCONT(int CONT) {
        this.CONT = CONT;
    }

}
