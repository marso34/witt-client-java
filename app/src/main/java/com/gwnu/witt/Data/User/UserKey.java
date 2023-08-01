package com.gwnu.witt.Data.User;

import com.google.gson.annotations.SerializedName;
public class UserKey {
    @SerializedName("USER_PK")
    int userPK;
    @SerializedName("BL_PK")
    private Integer BL_PK;

    public UserKey(int userPK) {
        this.userPK = userPK;
    }

    public UserKey(Integer BL_PK){
        this.BL_PK = BL_PK;
    }

    public int getPk(){
        return userPK;
    }
}
