package com.example.healthappttt.Data.Chat;

import com.google.gson.annotations.SerializedName;

public class MSD {
    @SerializedName("targetId")
    private String targetId;

    @SerializedName("massage")
    private String massage;

    public MSD(String targetId, String massage) {
        this.targetId = targetId;
        this.massage = massage;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
