package com.example.healthappttt.Activity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "ec2-43-200-245-144.ap-northeast-2.compute.amazonaws.com/";
    private static Retrofit retrofit = null;

    private RetrofitClient() {

    }

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //요청을 보낼 base url 설정
                    .addConverterFactory(GsonConverterFactory.create()) //json 파싱을 위한 컨버터 추가가
                   .build();
        }
        return retrofit;
    }

}
