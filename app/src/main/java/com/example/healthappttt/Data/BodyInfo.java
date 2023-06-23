package com.example.healthappttt.Data;

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

    public BodyInfo(String birthday, int gender, int height, int weight) {
        this.birthday = birthday;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
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
}
