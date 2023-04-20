package com.example.healthappttt.Activity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserKeyResponse {

    @SerializedName("message")
    private String userkey;

    @SerializedName("code")
    private int code;

    public String getUserName() {
        return userkey;
    }

    public String getUsercode() {
        return Integer.toString(code);
    }
}
