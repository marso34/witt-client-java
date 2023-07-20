package com.example.healthappttt.Data.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MSG {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;
    @SerializedName("myFlag")
    private int myFlag;
    @SerializedName("chatRoomId")
    private int chatRoomId;
    @SerializedName("message")
    private String message;
    @SerializedName("timeStamp")
    private String timestamp = "0000-00-00 00:00:00";
    private int success = -1;


    public MSG(int myFlag, int chatRoomId, String message, String timestamp,int success) {
        this.myFlag = myFlag;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.timestamp = timestamp;
        this.success = success;
    }
    public int getSuccess(){
        return success;
    }
    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp(){
        if(timestamp ==null) timestamp = "0000-00-00 00:00:00";
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getTimestamp: null");
        }

        long timeStamp = date.getTime(); // 날짜 및 시간을 밀리초로 변환
            System.out.println(timestamp);
            return timeStamp;

    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public int getMyFlag() {
        return myFlag;
    }
}
