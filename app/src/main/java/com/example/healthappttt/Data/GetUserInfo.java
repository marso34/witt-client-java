package com.example.healthappttt.Data;

import com.example.healthappttt.Fragment.ApiClient;
import com.example.healthappttt.Fragment.ApiInterface;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;

public class GetUserInfo {
    private int USER_PK;
    private String Email;
    private String User_NM;

    public int getUserPk() {
        return USER_PK;
    }

    public void setUserPk(int userPk) {
        this.USER_PK = userPk;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getUserNm() {
        if (User_NM == null || User_NM.isEmpty()) {
            return "Unknown";
        }
        return User_NM;
    }

    public void setUserNm(String userNm) {
        this.User_NM = userNm;
    }

    public GetUserInfo(int USER_PK, String Email, String User_NM) {
        this.USER_PK = USER_PK;
        this.Email = Email;
        this.User_NM = User_NM;
    }

    public static void getUserInfo(String email, Callback<GetUserInfo> callback) {
        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<GetUserInfo> call = apiInterface.getUserInfo(email);
        call.enqueue(callback);
    }
}