package com.example.healthappttt.Activity;

import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity2 {

    //api 요청 인터페이스 가져오기
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class); // create메서드로 api서비스 인터페이스의 구현제 생성




    //API 요청 매서드 호출
    private void getuserkey(UserKey data) {
        apiService.getUsers(data).enqueue(new Callback<UserKeyResponse>() {
            @Override
            public void onResponse(Call<UserKeyResponse> call, Response<UserKeyResponse> response) {

                // 서버에서 받은 응답을 처리하는 코드를 작성합니다.
                if ( response.isSuccessful() ) {
                    UserKeyResponse result = response.body();

                    if(result != null) {
                        //서버에서 반환된 값이 null이 아닌 경우 처리할 코드
                        Log.d("MainActivity2", "Userkey: " + result.getUserkey());
                    } else {
                        //서버에서 반환된 값이 null인 경우 처리할 코드
                        Log.d("MainActivity2", "Response body is null");
                    }
                } else {
                    // 서버 응답이 실패했을때
                    Log.d("MainActivity2","서버 응답 실패. 상태코드:" + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserKeyResponse> call, Throwable t) {
                // API 호출에 실패한 경우 처리합니다.
                Log.d("MainActivity2", "API호출 실패");
            }

        });
    }



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
