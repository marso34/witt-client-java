package com.example.healthappttt.Fragment;

import com.example.healthappttt.Data.NearUsersData;
import com.example.healthappttt.Data.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/GetNearUsers")
    Call<List<UserInfo>> GetNearUsers(@Body NearUsersData data);
}