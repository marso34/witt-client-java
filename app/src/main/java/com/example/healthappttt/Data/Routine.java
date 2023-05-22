package com.example.healthappttt.Data;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routine implements Serializable {
    private int ID;
    private String startTime;
    private String endTime;
    private String runTime;
    private int cat;
    private int dayOfWeek;

    public Routine() {}

    public Routine(String startTime, String endTime, int cat, int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Routine(int ID, String startTime, String endTime, int cat, int dayOfWeek) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Routine(int ID, String startTime, String endTime, String runTime, int cat, int dayOfWeek) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
    }
    public Routine(Routine r) {
        this.ID = r.ID;
        this.dayOfWeek = r.dayOfWeek;
        this.cat = r.cat;
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
    public int getCat()                                       { return this.cat; }
    public String getStartTime()                              { return this.startTime; }
    public String getEndTime()                                { return this.endTime;}
    public String getRunTime()                                { return this.runTime; }
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
    public void setCat(int cat)                               { this.cat = cat; }
    public void setStartTime(String startTime)                { this.startTime = startTime; }
    public void setEndTime(String endTime)                    { this.endTime = endTime; }
    public void setRunTime(String runTime)                    { this.runTime = runTime; }
}



