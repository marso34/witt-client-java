package com.example.healthappttt.Activity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserKeyResponse {

    @SerializedName("code")
    private String userkey;

    @SerializedName("message")
    private int code;

    public String getUserName() {
        return userkey;
    }

    public String getUsercode() {
        return Integer.toString(code);
    }
}
