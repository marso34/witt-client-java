package com.example.healthappttt.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

public class PreferenceHelper
{
    private final String NAME = "name";
    //    private UserProfile upf = new UserProfile();   테스트
    private SharedPreferences app_prefs;
    private Context context;
    private UserClass userClass;


    public PreferenceHelper(String file_name,Context context) {
        app_prefs = context.getSharedPreferences(file_name, Context.MODE_PRIVATE);
    }

    public PreferenceHelper(String file_name) {
        app_prefs = context.getSharedPreferences(file_name,Context.MODE_PRIVATE);
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
//    차단목록 추가 매서드
//    public void putBlackList( String name) {
//        SharedPreferences.Editor edit = app_prefs.edit();
//        edit.putString("BlackUser"+name,name);
//        Log.d("putblacklist에 저장:",getBlackUser(name));
//        edit.apply();
//    }
    public void putMembership(UserClass userClass) {
        SharedPreferences.Editor edit = app_prefs.edit();
        UserData userData = userClass.getUserInfo();
        PhoneInfo phoneInfo = userClass.getPhoneInfo();
        MannerInfo mannerInfo = userClass.getMannerInfo();
        LocInfo locInfo = userClass.getLocInfo();
        ExPerfInfo exPerfInfo = userClass.getExPerfInfo();
        BodyInfo bodyInfo = userClass.getBodyInfo();
        /**
         * UserData 저장
         **/
        edit.putString("email", userData.getEmail());
        edit.putInt("platform",userData.getPlatform());
        edit.putString("name",userData.getName());
        edit.putString("image",userData.getImage());
        /**
         * PhoneInfo 저장
         **/
        edit.putString("phoneNumber",phoneInfo.getPhoneNumber());
        edit.putString("deviceModel",phoneInfo.getDeviceModel());
        edit.putString("deviceId",phoneInfo.getDeviceId());
        /**
         * MannerInfo 저장
         **/
        edit.putInt("reliabilityValue",mannerInfo.getReliabilityValue());
        edit.putInt("sincerityValue",mannerInfo.getSincerityValue());
        edit.putInt("kindnessValue",mannerInfo.getKindnessValue());
        /**
         * locInfo 저장
         **/
        edit.putFloat("userLat", (float) locInfo.getUserLat());
        edit.putFloat("userLon", (float) locInfo.getUserLon());
        edit.putString("gymNm",locInfo.getGymNm());
        edit.putFloat("gymLat", (float) locInfo.getGymLat());
        edit.putFloat("gymLon", (float) locInfo.getGymLon());
        /**
         * exPerfInfo 저장
         **/
        edit.putInt("benchValue",exPerfInfo.getBenchValue());
        edit.putInt("squatValue",exPerfInfo.getSquatValue());
        edit.putInt("deadliftValue",exPerfInfo.getDeadliftValue());
        /**
         * bodyInfo 저장
         **/
        edit.putString("birthday",bodyInfo.getBirthday());
        edit.putInt("gender",bodyInfo.getGender());
        edit.putInt("height", (int) bodyInfo.getHeight());
        edit.putInt("weight", (int) bodyInfo.getWeight());

        edit.apply();
        Log.d("sharedpref","로컬 저장 완료 ");
    }

    //필요한 변수 getter 매소드 생성
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

