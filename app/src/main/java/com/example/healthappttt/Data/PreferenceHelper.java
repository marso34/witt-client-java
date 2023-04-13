package com.example.healthappttt.Data;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class PreferenceHelper
{
    private final String INTRO = "intro";
    private final String NAME = "name";
    private final String HOBBY = "hobby";
    private SharedPreferences app_prefs;
    private Context context;

    //getsh

    public PreferenceHelper(Context context)
    {
        //shared_prefs 디렉토리 안에 mydir1과 mydir2 디렉토리 생성( 중복 X )
        File dir1 = new File(getSharedPreferencesPath(context), "mydir1");
        if (!dir1.exists()) {
            dir1.mkdir();
        }

        File dir2 = new File(getSharedPreferencesPath(context),"mydir2");
        if (!dir2.exists()) {
            dir2.mkdir();
        }
        this.context = context;
    }

    //SharedPreferences 경로 설정
    private String getSharedPreferencesPath(Context context) {
        return context.getFilesDir().getParentFile().getPath() + "/shared_prefs";
    }

    //닉네임, 프로필이미지, 키, 몸무게, 헬스장, 운동 수행능력 서버에 저장 및 출력 가능 매소드들
    public void putNickName(String nickname) {
        //mydir1 디렉토리아래 UserInfo 파일 생성 후 NAME: nickname 형태로 저장
        app_prefs = context.getSharedPreferences("mydir1/UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, nickname);
        edit.apply();
    }
    public String getNickName() { return app_prefs.getString(NAME,"");}



    //



//----------------------------------------------------------------------


    public void putIsLogin(boolean loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginOrOut);
        edit.apply();
    }

    public void putName(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginOrOut);
        edit.apply();
    }

    public String getName()
    {
        return app_prefs.getString(NAME, "");
    }

    public void putHobby(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(HOBBY, loginOrOut);
        edit.apply();
    }

    public String getHobby()
    {
        return app_prefs.getString(HOBBY, "");
    }
}
