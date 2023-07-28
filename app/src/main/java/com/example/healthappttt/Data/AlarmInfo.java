package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class AlarmInfo {
    @SerializedName("NOTIFY_PK")
    private int NotifyKey;

    @SerializedName("User_FK")
    private int UserKey;

    @SerializedName("OUser_FK")
    private int OUserKey;
    @SerializedName("CAT")
    private int Cat;
    @SerializedName("Read_TS")
    private String Read_TS;

    @SerializedName("TS")
    private String TS;
    @SerializedName("Link")
    private int Link;// CHAT_PK OR CHAT_ROOM_PK OR REVIEW_PK OR REPORT_PK...
    //LINK로 얻어온 데이터로 TITLE,CONTENT채우기.

    public AlarmInfo(int notifyKey, int userKey, int OUserKey, int link, int cat, String read_TS, String TS) {
        NotifyKey = notifyKey;
        UserKey = userKey;
        this.OUserKey = OUserKey;
        Link = link;
        Cat = cat;
        Read_TS = read_TS;
        this.TS = TS;
    }


    public int getNotifyKey(){ return NotifyKey; }
    public int getUserKey() {
        return UserKey;
    }

    public void setUserKey(int userKey) {
        UserKey = userKey;
    }

    public int getOUserKey() {
        return OUserKey;
    }

    public void setOUserKey(int OUserKey) {
        this.OUserKey = OUserKey;
    }

    public int getLink() {
        return Link;
    }

    public void setLink(int link) {
        Link = link;
    }

    public int getCat() {
        return Cat;
    }

    public void setCat(int cat) {
        Cat = cat;
    }

    public String getRead_TS() {
        return Read_TS;
    }

    public void setRead_TS(String read_TS) {
        Read_TS = read_TS;
    }

    public String getTS() {
        return TS;
    }

    public void setTS(String TS) {
        this.TS = TS;
    }
}