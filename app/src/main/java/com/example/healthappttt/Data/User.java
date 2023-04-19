package com.example.healthappttt.Data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String ProfileImg;

    private Integer Distance = 0;

    public User(int user_pk, String user_nm, String user_img, String gym_nm, double user_lat, double user_lon, String start_time, String end_time, int cat) {
        this.UserKey = user_pk;
        this.name = user_nm;
        this.ProfileImg = user_img;
        this.gymName = gym_nm;
        this.latitude = user_lat;
        this.longitude = user_lon;
        this.routineCategory = cat;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            this.startTime = sdf.parse(start_time);
            this.endTime = sdf.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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