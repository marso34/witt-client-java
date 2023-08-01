package com.gwnu.witt.Data.User;

import com.google.gson.annotations.SerializedName;

public class PhoneInfo {
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("deviceModel")
    private String deviceModel;

    @SerializedName("deviceId")
    private String deviceId;

    public PhoneInfo(String phoneNumber, String deviceModel, String deviceId) {
        this.phoneNumber = phoneNumber;
        this.deviceModel = deviceModel;
        this.deviceId = deviceId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
