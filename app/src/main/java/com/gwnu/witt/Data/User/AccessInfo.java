package com.gwnu.witt.Data.User;


import com.google.gson.annotations.SerializedName;

public class AccessInfo {

    @SerializedName("accessTime")
    private long accessTime;
    @SerializedName("logoutTime")
    private long logoutTime;
    public AccessInfo(long accessTime,long logoutTime) {
        this.accessTime = accessTime;
        this.logoutTime = logoutTime;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }
}
