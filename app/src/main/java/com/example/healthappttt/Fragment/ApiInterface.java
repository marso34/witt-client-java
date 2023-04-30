package com.example.healthappttt.Fragment;

import com.example.healthappttt.Data.GetUserInfo;
import com.example.healthappttt.Data.NearUsersData;
import com.example.healthappttt.Data.UserData;
import com.example.healthappttt.Data.UserInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("/GetNearUsers")
    Call<List<UserInfo>> GetNearUsers(@Body NearUsersData data);
    @POST("/saveUser")
    Call<ResponseBody> sendData(@Body UserData data);

    @GET("/getUserInfo/{useremail}")
    Call<GetUserInfo> getUserInfo(@Path("useremail") String useremail);

}