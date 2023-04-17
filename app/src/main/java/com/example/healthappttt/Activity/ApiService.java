package com.example.healthappttt.Activity;

import com.example.healthappttt.Activity.UserKeyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/GetNearUsers")
    Call<UserKeyResponse> getuserkey(); // 함수 확인 필요
    //Call<UserKeyResponse> XXXXXXX -> String 형식으로 바꿈 (어차피 UserKeyResponse도 String으로 받는거라서)

}
//
//        : GET - Query 형태로 보냄
//
//        : POST - Field 형태로 보내기 때문에 @FormUrlEncoded 어노테이션 붙여줘야함
//
//        : PUT - 마찬가지로 Field 형태가 있어서 어노테이션 추가, 경로를 통해 id를 보냄
//
//        : DELETE - 경로를 통해 id를 보냄
//
//        => 통신방법에 따라 보내는 형태가 다름 주의!
