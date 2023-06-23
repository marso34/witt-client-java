package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("email")
    private String email;

    @SerializedName("platform")
    private Integer platform;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public UserData(String email, Integer platform, String name, String image) {
        this.email = email;
        this.platform = platform;
        this.name = name;
        this.image = image;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
