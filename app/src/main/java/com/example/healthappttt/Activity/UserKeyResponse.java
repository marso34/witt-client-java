package com.example.healthappttt.Activity;

import com.google.gson.annotations.SerializedName;

public class UserKeyResponse {

    @SerializedName("userkey")
    private String userkey;

    public String getUserkey() {
        return userkey;
    }
}
