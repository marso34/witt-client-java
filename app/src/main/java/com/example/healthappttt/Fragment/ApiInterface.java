package com.example.healthappttt.Fragment;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/GetNearUsers")
    Call<String> GetNearUsers();


}