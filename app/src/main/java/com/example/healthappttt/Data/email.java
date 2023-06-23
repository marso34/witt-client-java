package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class email {
    @SerializedName("email")
    String email;

    public email(String email){
        this.email = email;
    }
}
