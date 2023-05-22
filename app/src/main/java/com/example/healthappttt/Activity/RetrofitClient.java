package com.example.healthappttt.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://43.200.245.144:3000/";
    private static Retrofit retrofit = null;


    private RetrofitClient() {

    }

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //요청을 보낼 base url 설정
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)) //json 파싱을 위한 컨버터 추가가
                   .build();
        }
        return retrofit;
    }

}
