package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class AlarmInfo {
    @SerializedName("NotifyKey")
    private long NotifyKey;

    @SerializedName("UserKey")
    private long UserKey;

    @SerializedName("OUserKey")
    private long OUserKey;

    @SerializedName("Link")
    private int Link;

    @SerializedName("Cat")
    private int Cat;

    @SerializedName("Read_TS")
    private int Read_TS;

    @SerializedName("TS")
    private int TS;

    public long getNotifyKey() {
        return NotifyKey;
    }

    public void setNotifyKey(long notifyKey) {
        NotifyKey = notifyKey;
    }

    public long getUserKey() {
        return UserKey;
    }

    public void setUserKey(long userKey) {
        UserKey = userKey;
    }

    public long getOUserKey() {
        return OUserKey;
    }

    public void setOUserKey(long OUserKey) {
        this.OUserKey = OUserKey;
    }

    public double getLink() {
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

    public int getRead_TS() {
        return Read_TS;
    }

    public void setRead_TS(int read_TS) {
        Read_TS = read_TS;
    }

    public int getTS() {
        return TS;
    }

    public void setTS(int TS) {
        this.TS = TS;
    }
}