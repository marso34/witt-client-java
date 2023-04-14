package com.example.healthappttt.Activity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserKeyResponse {

    @SerializedName("userkey")
    private String userkey;

    public String getUserName() {
        return userkey;
    }
}
