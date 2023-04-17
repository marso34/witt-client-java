package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
public class User implements Serializable {
    private long UserKey;
    private String name;
    private String gymName;
    private double latitude;
    private double longitude;
    private int routineCategory;
    private Date startTime;
    private Date endTime;
    private String ProfileImg = "";
    private Integer Distance = 0;
    private ArrayList<Exercise> exercises;

    public User(long userKey, String name, String gymName, double latitude, double longitude, int routineCategory, Date startTime, Date endTime, String profileImg, Integer distance) {
        UserKey = userKey;
        this.name = name;
        this.gymName = gymName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routineCategory = routineCategory;
        this.startTime = startTime;
        this.endTime = endTime;
        ProfileImg = profileImg;
        Distance = distance;
    }

    public long getUserKey() {
        return UserKey;
    }

    public void setUserKey(long userKey) {
        UserKey = userKey;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }

    public Integer getDistance() {
        return Distance;
    }

    public void setDistance(Integer distance) {
        Distance = distance;
    }
}