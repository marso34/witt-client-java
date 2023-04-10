package com.example.healthappttt;

import com.example.healthappttt.Data.User;
import com.example.healthappttt.Data.UserKey;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/Witt/getuserhw")
    Call<UserKey> getUsers(); // 함수 확인 필요


    @GET("/retrofit/get")
    Call<ResponseBody> getFunc(@Query("data") String data);

    @FormUrlEncoded
    @POST("/retrofit/post")
    Call<ResponseBody> postFunc(@Field("data") String data);

    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
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
