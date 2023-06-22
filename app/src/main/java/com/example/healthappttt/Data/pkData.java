package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class pkData {
    @SerializedName("ID")
    private int ID;
    @SerializedName("BL_PK")
    private Integer BL_PK;

    public pkData(int ID) {
        this.ID = ID;
    }
    public pkData(Integer BL_PK){
        this.BL_PK = BL_PK;
    }
}
