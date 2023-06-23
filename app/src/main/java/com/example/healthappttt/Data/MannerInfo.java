package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class MannerInfo {
    @SerializedName("reliabilityValue")
    private int reliabilityValue;

    @SerializedName("sincerityValue")
    private int sincerityValue;

    @SerializedName("kindnessValue")
    private int kindnessValue;

    public MannerInfo(int reliabilityValue, int sincerityValue, int kindnessValue) {
        this.reliabilityValue = reliabilityValue;
        this.sincerityValue = sincerityValue;
        this.kindnessValue = kindnessValue;
    }

    public double getReliabilityValue() {
        return reliabilityValue;
    }

    public void setReliabilityValue(int reliabilityValue) {
        this.reliabilityValue = reliabilityValue;
    }

    public double getSincerityValue() {
        return sincerityValue;
    }

    public void setSincerityValue(int sincerityValue) {
        this.sincerityValue = sincerityValue;
    }

    public double getKindnessValue() {
        return kindnessValue;
    }

    public void setKindnessValue(int kindnessValue) {
        this.kindnessValue = kindnessValue;
    }
}