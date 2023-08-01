package com.gwnu.witt.Data.Exercise;

import com.google.gson.annotations.SerializedName;

public class GetRoutine {
    @SerializedName("ID")
    private int ID;

    @SerializedName("dayOfWeek")
    private int dayOfWeek;

    public GetRoutine(int ID, int dayOfWeek) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
    }
}