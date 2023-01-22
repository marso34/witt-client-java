package com.example.healthappttt.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;// 파이어 베이스의 auth기능의 접급권한을 갖는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//activity_login_layout과 연결됨.

        mAuth = FirebaseAuth.getInstance();// 파이어베이스의 auth기능의 접근 권한을 갖는변수

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        //findViewById = >activity_login_layout에서 "loginbutton" id를 가진
        //컴포넌트를 찾음
        findViewById(R.id.gotoPasswordResetButton).setOnClickListener(onClickListener);
        //findViewById = >activity_login_layout에서 "gotoPasswordResetButton" id를 가진
        //컴포넌트를 찾음
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {// 클릭됐을시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginButton:// 클릭된것이 로그인버튼일시.
                    login();
                    break;
                case R.id.gotoPasswordResetButton:// 클릭된것이 패스워드 버튼일시
                   // myStartActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    private void login() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();//emailEditText 컴포넌트에서 아이디 문자열 가져오기
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();// passwordEditText컴포넌트에서 비번 문자열 가져오기

        if (email.length() > 0 && password.length() > 0) {//이메일과 비번의 길이가 둘 다 0보다 크면
            final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);//변수에 loaderLyaout id를 가진 컴포넌트 할당
            loaderLayout.setVisibility(View.VISIBLE);//loaderLyaout을 보이게함 (공간차지)

        } else {
//            Util.showToast(LoginActivity.this, "이메일 또는 비밀번호를 입력해 주세요.");//이메일이나 패스워드가 입력안됐을때 메세지 출력(첫한순간에)
        }
    }

    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
