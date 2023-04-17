package com.example.healthappttt.Fragment;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/GetNearUsers")
    Call<String> GetNearUsers();
}