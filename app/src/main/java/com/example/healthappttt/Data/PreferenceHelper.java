package com.example.healthappttt.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.healthappttt.Data.User.BodyInfo;
import com.example.healthappttt.Data.User.ExPerfInfo;
import com.example.healthappttt.Data.User.LocInfo;
import com.example.healthappttt.Data.User.MannerInfo;
import com.example.healthappttt.Data.User.PhoneInfo;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.Data.User.UserData;
import com.example.healthappttt.Data.User.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class PreferenceHelper
{
    private final String NAME = "name";
    private SharedPreferences app_prefs;
    private Context context;
    private UserClass userClass;


    public PreferenceHelper(String file_name,Context context) {
        app_prefs = context.getSharedPreferences(file_name, Context.MODE_PRIVATE);
    }

    public PreferenceHelper(String file_name) {
        app_prefs = context.getSharedPreferences(file_name,Context.MODE_PRIVATE);
    }

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
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
        Log.d("putProfile","pk저장" );
        // User_Img 는 byte[] 타입이므로 Base64로 인코딩하여 저장
//        if(upf.getUser_Img() != null) {
//            String encodedImage = Base64.encodeToString(upf.getUser_Img(), Base64.DEFAULT);
//            edit.putString("User_Img", encodedImage);
//        }

        edit.apply();
    }

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
        edit.putString("User_NM",userData.getName());
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
        edit.putInt("totalValue",exPerfInfo.getBenchValue() + exPerfInfo.getSquatValue() + exPerfInfo.getDeadliftValue());
        /**
         * bodyInfo 저장
         **/
        edit.putString("birthday",bodyInfo.getBirthday());
        edit.putInt("gender",bodyInfo.getGender());
        edit.putInt("height", (int) bodyInfo.getHeight());
        edit.putInt("weight", (int) bodyInfo.getWeight());
        edit.putInt("temp",bodyInfo.getIsPublic());

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
    public String getUser_NM() {
        return app_prefs.getString("User_NM","__");
    }
    public String getGYMNM() {
        return app_prefs.getString("gymNm","");
    }
    public int getGender() {
        return app_prefs.getInt("gender",0);
    }
    public int getheight() {
        return app_prefs.getInt("height",1);
    }
    public int getweight() {
        return app_prefs.getInt("weight",1);
    }
    public int gettemp() {
        return app_prefs.getInt("temp",1);
    }

    public int getsquatValue() {
        return app_prefs.getInt("squatValue",1);
    }
    public int getbenchValue() {
        return app_prefs.getInt("benchValue",1);
    }
    public int getdeadValue() {
        return app_prefs.getInt("deadValue",1);
    }

    //필요한 변수 setter 매소드 생성
    public void setPK(int pk) {
        app_prefs.edit().putInt("USER_PK",pk);
        app_prefs.edit().apply();
        Log.d("setPK: ", String.valueOf(pk));
    }
    public void setBodyInfo(int height, int weight, int temp) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putInt("height",height);
        edit.putInt("weight",weight);
        edit.putInt("temp",temp);
        edit.apply();
    }
    public void setWeight(int squat,int bench, int deadlift) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putInt("squatValue",squat);
        edit.putInt("benchValue",bench);
        edit.putInt("deadValue",deadlift);
        Log.d("squatValue: ", String.valueOf(squat)); //테스트용
        edit.apply();
    }



    //

    public  Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("User_NM",app_prefs.getString("User_NM",""));
        userData.put("gymNm",app_prefs.getString("gymNm","헬스장을 등록해주세요"));
        userData.put("gender", app_prefs.getInt("gender", 0));
        userData.put("temp", app_prefs.getInt("temp", 0));
        userData.put("height", app_prefs.getInt("height", 0));
        userData.put("weight", app_prefs.getInt("weight", 0));
        userData.put("squatValue", app_prefs.getInt("squatValue", 0));
        userData.put("benchValue", app_prefs.getInt("benchValue", 0));
        userData.put("deadValue", app_prefs.getInt("deadValue", 0));
        //userData.put("totalValue",app_prefs.getInt("totalValue",0));
        //userData.put("image", app_prefs.getString("image", ""));
        Log.d("member getUserData() ", String.valueOf(app_prefs.getInt("height", 0)));
        return userData;
    }
    public void putUserDefault(Map<String, Object> userdefault){
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("User_NM", String.valueOf(userdefault.get("User_NM")));
        edit.putInt("gender", (Integer) userdefault.get("gender"));
        edit.putInt("height", (Integer) userdefault.get("height"));
        edit.putInt("weight", (Integer) userdefault.get("weight"));
        edit.putInt("squatValue", (Integer) userdefault.get("squatValue"));
        edit.putInt("benchValue", (Integer) userdefault.get("benchValue"));
        edit.putInt("deadValue", (Integer) userdefault.get("deadValue"));
        edit.putInt("totalValue",(Integer) userdefault.get("totalValue"));
        edit.apply();
        Log.d("putUserDefault","유저 기본 정보 로컬 저장 완료 ");

    }

    public void deleteUserTB() {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.clear();
        editor.apply();
    }


}