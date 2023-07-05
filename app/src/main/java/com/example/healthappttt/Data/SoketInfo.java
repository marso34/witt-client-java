package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class SoketInfo {
    
    @SerializedName("soket_ID")
    private String Soket_ID;
    @SerializedName("userKey")
    private String UserKey;

    public SoketInfo(String UserKey) {
        this.UserKey = UserKey;
    }

    public String getSoket_ID() {
        return Soket_ID;
    }

    public void setSoket_ID(String Soket_ID) {
        this.Soket_ID = Soket_ID;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String UserKey) {
        this.UserKey = UserKey;
    }
}
