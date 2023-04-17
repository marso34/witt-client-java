package com.example.healthappttt.Data;

import java.util.Date;

public class UserDetail extends User{
    private double benchPress;
    private double squat;
    private double deadlift;
    private int trustworthiness;
    private int diligence;
    private int kindness;
    private double height;
    private double weight;

    public UserDetail(long UserKey, String name, String gymName, double latitude, double longitude, int routineCategory, Date startTime, Date endTime, String ProfileImg, Integer Distance, double benchPress, double squat, double deadlift, int trustworthiness, int diligence, int kindness, double height, double weight) {
        super(UserKey, name, gymName, latitude, longitude, routineCategory, startTime, endTime, ProfileImg, Distance);
        this.benchPress = benchPress;
        this.squat = squat;
        this.deadlift = deadlift;
        this.trustworthiness = trustworthiness;
        this.diligence = diligence;
        this.kindness = kindness;
        this.height = height;
        this.weight = weight;
    }

    public double getBenchPress() {
        return benchPress;
    }

    public void setBenchPress(double benchPress) {
        this.benchPress = benchPress;
    }

    public double getSquat() {
        return squat;
    }

    public void setSquat(double squat) {
        this.squat = squat;
    }

    public double getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(double deadlift) {
        this.deadlift = deadlift;
    }

    public int getTrustworthiness() {
        return trustworthiness;
    }

    public void setTrustworthiness(int trustworthiness) {
        this.trustworthiness = trustworthiness;
    }

    public int getDiligence() {
        return diligence;
    }

    public void setDiligence(int diligence) {
        this.diligence = diligence;
    }

    public int getKindness() {
        return kindness;
    }

    public void setKindness(int kindness) {
        this.kindness = kindness;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

