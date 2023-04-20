package com.example.healthappttt.Activity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserKeyResponse {

    @SerializedName("message")
    private String userkey;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private int message;

    public String getUsermessage() {
        return Integer.toString(message);
    }

    public int getUsercode() {
        return code;
    }

    /*private class MyMessage {
        @SerializedName("message")
        private int message;

        public int getMessage() {
            return message;
        }
    }*/
}
