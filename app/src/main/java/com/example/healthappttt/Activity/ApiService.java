package com.example.healthappttt.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/getuserkey")
    Call<List<UserProfile>> getuserprofile(); // 파라미터(User_PK)를 줘서 동적으로 변경 필요

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
