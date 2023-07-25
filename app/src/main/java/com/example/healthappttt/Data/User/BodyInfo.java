package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class BodyInfo {

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("gender")
    private int gender;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("isPublic")
    private int isPublic;

    public BodyInfo(String birthday, int gender, int height, int weight, int isPublic) {
        this.birthday = birthday;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.isPublic = isPublic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsPublic() {
        return isPublic;
    }
}
