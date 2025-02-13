package com.gwnu.witt.Data.User;

import com.google.gson.annotations.SerializedName;
public class UserClass {
    @SerializedName("userData")
    private UserData userData;

    @SerializedName("phoneInfo")
    private PhoneInfo phoneInfo;

    @SerializedName("mannerInfo")
    private MannerInfo mannerInfo;

    @SerializedName("locInfo")
    private LocInfo locInfo;

    @SerializedName("exPerfInfo")
    private ExPerfInfo exPerfInfo;

    @SerializedName("bodyInfo")
    private BodyInfo bodyInfo;

    public UserClass(UserData userData, PhoneInfo phoneInfo, MannerInfo mannerInfo, LocInfo locInfo, ExPerfInfo exPerfInfo, BodyInfo bodyInfo) {
        this.userData = userData;
        this.phoneInfo = phoneInfo;
        this.mannerInfo = mannerInfo; // 회원가입 할 때 유저 매너를 보낼 이유가 없는데 왜 보내? 매너 테이블에 디폴트 값 넣고 그냥 생성해야지
        this.locInfo = locInfo;
        this.exPerfInfo = exPerfInfo;
        this.bodyInfo = bodyInfo;
    }

    public UserData getUserInfo() {
        return userData;
    }

    public void setUserInfo(UserData userInfo) {
        this.userData = userData;

    }

    public PhoneInfo getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(PhoneInfo phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public MannerInfo getMannerInfo() {
        return mannerInfo;
    }

    public void setMannerInfo(MannerInfo mannerInfo) {
        this.mannerInfo = mannerInfo;
    }

    public LocInfo getLocInfo() {
        return locInfo;
    }

    public void setLocInfo(LocInfo locInfo) {
        this.locInfo = locInfo;
    }

    public ExPerfInfo getExPerfInfo() {
        return exPerfInfo;
    }

    public void setExPerfInfo(ExPerfInfo exPerfInfo) {
        this.exPerfInfo = exPerfInfo;
    }

    public BodyInfo getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(BodyInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

}