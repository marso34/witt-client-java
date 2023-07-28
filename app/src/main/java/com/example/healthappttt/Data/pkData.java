package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class pkData {
    @SerializedName("ID")
    private int ID;
    @SerializedName("PK")
    private int PK;
    public pkData(int ID) {
        this.ID = ID;
    }
    public pkData(int ID, int PK){
        this.ID = ID;
        this.PK = PK;
    }
    public int getPk(){
        return ID;
    }
}
