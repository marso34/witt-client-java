package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;
public class UserKey {
    @SerializedName("USER_PK")
    int userPK;

    public UserKey(int userPK) {
        this.userPK = userPK;
    }

    public int getUserPK() {
        return userPK;
    }


//    public String getKey() {
//        return userkey;
//    }

}
