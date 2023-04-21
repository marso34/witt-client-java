package com.example.healthappttt.Data;


import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("UserKey")
    private long userKey;
    @SerializedName("Name")
    private String name;
    @SerializedName("GymName")
    private String gymName;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("RoutineCategory")
    private int routineCategory;
    @SerializedName("StartTime")
    private String startTime;
    @SerializedName("EndTime")
    private String endTime;
    @SerializedName("DayOfWeek")
    private int dayOfWeek;

    private Integer distance = 0;

    // Getters and Setters
    public long getUserKey() {
        return userKey;
    }
    public void setDistance(Integer D){
        distance = D;
    }
    public Integer getDistance(){
        return distance;
    }


    public void setUserKey(long userKey) {
        this.userKey = userKey;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGymName() {
        return gymName;
    }
    public void setGymName(String gymName) {
        this.gymName = gymName;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public int getRoutineCategory() {
        return routineCategory;
    }
    public void setRoutineCategory(int routineCategory) {
        this.routineCategory = routineCategory;
    }
    public String getStartTime() {
        String[] timeParts = startTime.split(":"); // ":" 기준으로 문자열을 분리하여 배열에 저장
// 분과 초를 추출하여 각각 정수형 변수에 저장
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);
        String convertedTime = String.format("%02d:%02d", minutes, seconds); // mm:ss 형식으로 변환
        return convertedTime; // 변환된 시간 출력
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        String[] timeParts = endTime.split(":"); // ":" 기준으로 문자열을 분리하여 배열에 저장
// 분과 초를 추출하여 각각 정수형 변수에 저장
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);
        String convertedTime = String.format("%02d:%02d", minutes, seconds); // mm:ss 형식으로 변환
        return convertedTime; // 변환된 시간 출력
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
