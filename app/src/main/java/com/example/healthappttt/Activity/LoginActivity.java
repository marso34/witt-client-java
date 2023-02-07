package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
        findViewById(R.id.btnSignUp).setOnClickListener(onClickListener);

        //findViewById = >activity_login_layout에서 "gotoPasswordResetButton" id를 가진
        //컴포넌트를 찾음

//        userIsLoggedIn(); //자동로그인
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
                case R.id.btnSignUp:// 클릭된것이 패스워드 버튼일시
                    myStartActivity(signupActivity.class);
                    break;
            }
        }
    };

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }

    private void login() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();//emailEditText 컴포넌트에서 아이디 문자열 가져오기
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();// passwordEditText컴포넌트에서 비번 문자열 가져오기
        if (email.length() > 0 && password.length() > 0) {//이메일과 비번의 길이가 둘 다 0보다 크면
            final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);//변수에 loaderLyaout id를 가진 컴포넌트 할당
            loaderLayout.setVisibility(View.VISIBLE);//loaderLyaout을 보이게함 (공간차지)
            mAuth.signInWithEmailAndPassword(email, password)//파이어베이스 서버로 이메일과 비번을 보낸다. (맞나 틀리나 검증)체크
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {//람다식?
                            loaderLayout.setVisibility(View.GONE);//loaderLyaout 삭제함.(공간차지x)
                            if (task.isSuccessful()) {//로그인 검증이 성공하면
                                FirebaseUser user = mAuth.getCurrentUser();//현재유저의 db에 접근권한 활성화
                                finishAffinity();
                                myStartActivity(MainActivity.class);//메인엑티비티 실행
                            } else {
                                if (task.getException() != null) {
                                }
                            }
                        }
                    });
        } 
    }
    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}