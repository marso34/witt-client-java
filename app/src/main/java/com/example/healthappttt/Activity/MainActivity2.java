package com.example.healthappttt.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.ApiService;
import com.example.healthappttt.Data.PreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity2 extends AppCompatActivity {
    private ApiService apiService;
    private SharedPreferences prefs;
    private PreferenceHelper prefhelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //api 요청 인터페이스 가져오기
        apiService = RetrofitClient.getClient().create(ApiService.class); // create메서드로 api서비스 인터페이스의 구현제 생성
        //파라미터 넣어서 보내줘야하는데 뭘 보내야하지
        //getuserkey(); //유저키가 'a1234'
                        //로그인했을때 넘겨받는 정보를 파라미터로 넣는다.  email or phone_num 비교해서 해당하는 유저의 키를 받아온다.
                        //유저의 pk를 그대로 받을수있으면 필요가 없음 다른방향( 다른유저의 키를 가져오는 느낌)으로 가야함

    }


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
                        prefhelper.putNickName(String.valueOf(result)); //로컬에 이름(닉네임) 저장
                        Log.d("MainActivity2","user_name" + prefhelper.getName());
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
