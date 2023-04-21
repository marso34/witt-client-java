package com.example.healthappttt.Data;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routine implements Serializable {
    private int ID;
    private int dayOfWeek;
    private int exerciseCategories;
    private int startTime;
    private int endTime;
    private int runTime;


    public Routine() {}

    public Routine(int dayOfWeek, int exerciseCategories, int startTime, int endTime) {
        this.dayOfWeek = dayOfWeek;
        this.exerciseCategories = exerciseCategories;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Routine(int ID, int dayOfWeek, int exerciseCategories, int startTime, int endTime) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.exerciseCategories = exerciseCategories;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Routine(int ID, int dayOfWeek, int exerciseCategories, int startTime, int endTime, int runTime) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.exerciseCategories = exerciseCategories;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
    }
    public Routine(Routine r) {
        this.ID = r.ID;
        this.dayOfWeek = r.dayOfWeek;
        this.exerciseCategories = r.exerciseCategories;
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.runTime = r.runTime;
    }

    private String TimeToString(int Time) {
        String am_pm = "";

        if (Time >= 240) Time-= 240;

        if (Time < 120) {
            am_pm = "오전";
            if (Time < 10) Time += 120;
        } else {
            am_pm = "오후";
            if (Time >= 130) Time-= 120;
        }

        @SuppressLint("DefaultLocale") String result = String.format("%d:%02d", Time/10, Time % 10 * 6);

        return am_pm + " " + result;
    }


    public int getID()                                        { return this.ID; }
    public int getDayOfWeek()                                 { return this.dayOfWeek; }
    public int getExerciseCategories()                        { return this.exerciseCategories; }
    public int getStartTime()                                 { return this.startTime; }
    public int getEndTime()                                   { return this.endTime;}
    public int getRunTime()                                   { return this.runTime; }
    public String getStringStartTime()                        { return TimeToString(this.startTime); }
    public String getStringEndTime()                          { return TimeToString(this.endTime); }
    public String getStringRunTime()                          { return TimeToString(this.runTime); }
    public String getStringDayOfWeek() {
        switch (this.dayOfWeek) {
            case 0: return "일요일";
            case 1: return "월요일";
            case 2: return "화요일";
            case 3: return "수요일";
            case 4: return "목요일";
            case 5: return "금요일";
            case 6: return "토요일";
        }

        return null;
    }

    public void setID(int ID)                                 { this.ID = ID; }
    public void setDayOfWeek(int dayOfWeek)                   { this.dayOfWeek = dayOfWeek; }
    public void setExerciseCategories(int exerciseCategories) { this.exerciseCategories = exerciseCategories; }
    public void setStartTime(int startTime)                   { this.startTime = startTime; }
    public void setEndTime(int endTime)                       { this.endTime = endTime; }
    public void setRunTime(int runTime)                       { this.runTime = runTime; }
}



