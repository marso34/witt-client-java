package com.example.healthappttt.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class PreferenceHelper
{
    private final String NAME = "name";



    private SharedPreferences app_prefs;
    private Context context;
//    private UserProfile upf = new UserProfile();

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
    }


    //PK,email,IP,Platform,Name,PW,Img 로컬 저장 매소드
    public void putProfile(UserProfile upf) {

        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putInt( "USER_PK", upf.getUSER_PK());
        edit.putString("EMAIL", upf.getEMAIL());
        edit.putInt("IP", upf.getIP());
        edit.putInt("Platform", upf.getPlatform());
        edit.putString("User_NM", upf.getUser_NM());
        edit.putString("PW", upf.getPW());

        // User_Img 는 byte[] 타입이므로 Base64로 인코딩하여 저장
        if(upf.getUser_Img() != null) {
            String encodedImage = Base64.encodeToString(upf.getUser_Img(), Base64.DEFAULT);
            edit.putString("User_Img", encodedImage);
        }

        edit.apply();
    }

    public int getPK() {
        return  app_prefs.getInt( "USER_PK", 00);
    }
    public String getEmail() {
        return app_prefs.getString("EMAIL","__");
    }
    public int getIP() {
        return  app_prefs.getInt( "IP", 00);
    }
    public int getPlatform() {
        return  app_prefs.getInt( "Platform", 00);
    }
    public String getUser_NM() {
        return app_prefs.getString("User_NM","__");
    }
    public String getPW() {
        return app_prefs.getString("PW","__");
    }

}
//----------------------------------------------------------------------


//    public void putIsLogin(boolean loginOrOut)
//    {
//        SharedPreferences.Editor edit = app_prefs.edit();
//        edit.putBoolean(INTRO, loginOrOut);
//        edit.apply();
//    }
//
//    public void putName(String loginOrOut)
//    {
//        SharedPreferences.Editor edit = app_prefs.edit();
//        edit.putString(NAME, loginOrOut);
//        edit.apply();
//    }
//
//    public String getName()
//    {
//        return app_prefs.getString(NAME, "");
//    }
//
//    public void putHobby(String loginOrOut)
//    {
//        SharedPreferences.Editor edit = app_prefs.edit();
//        edit.putString(HOBBY, loginOrOut);
//        edit.apply();
//    }
//
//    public String getHobby()
//    {
//        return app_prefs.getString(HOBBY, "");
//    }

