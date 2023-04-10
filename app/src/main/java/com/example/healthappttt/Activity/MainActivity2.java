package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.ApiService;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.Data.UserKey;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity2  {
    private static final String URL = "ip-172-31-7-123.ap-northeast-2.compute.internal";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL) // api 엔드포인트의 기본 url 지정
            .addConverterFactory(GsonConverterFactory.create()) // 서버에서 받아온 json 데이터를 자바 객체로 변환하기 위해 Gson라이브러리 사용
            .build();
    //api 요청 인터페이스 가져오기
    ApiService apiService = retrofit.create(ApiService.class); // create메서드로 api서비스 인터페이스의 구현제 생성

    //api 요청 메서드 호출하기
    Call<UserKey> call = apiService.getUsers();
    call.enqueue

}










//    //api 호출하기
//    Call<List<User>> call = apiService.getUserkey();
//
//    call.enqueue(new Callback<List<User>>() {
//        @Override
//        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//            // 서버에서 받은 응답을 처리하는 코드를 작성합니다.
//            if (response.isSuccessful()) {
//                List<User> users = response.body();
//                // 서버에서 받은 데이터를 사용합니다.
//            } else {
//                // 서버에서 오류 응답을 받은 경우 처리합니다.
//            }
//        }
//
//        @Override
//        public void onFailure(Call<List<User>> call, Throwable t) {
//            // API 호출에 실패한 경우 처리합니다.
//        }
//    });
//}
