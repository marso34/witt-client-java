package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoutineData implements Serializable {
    @SerializedName("ID")
    private int ID;

    @SerializedName("userID")
    private int userID;

    @SerializedName("dayOfWeek")
    private int dayOfWeek;

    @SerializedName("cat")
    private int cat;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("exercises")
    private List<ExerciseData> exercises;

    public RoutineData() {}

    public RoutineData(int ID, int userID, int dayOfWeek, int cat, String startTime, String endTime, List<ExerciseData> exercises) {
        this.ID = ID;
        this.userID = userID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exercises = exercises;
    }

    public RoutineData(int ID, int dayOfWeek, int cat, String startTime, String endTime, List<ExerciseData> exercises) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exercises = exercises;
    }

    public RoutineData(String startTime, String endTime, int cat, int dayOfWeek, List<ExerciseData> exercises) {
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exercises = exercises;
    }

    public RoutineData(int ID, String startTime, String endTime, int cat, int dayOfWeek) {
        this.ID = ID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public RoutineData(RoutineData r) {
        this.ID = r.ID;
        this.userID = r.userID;
        this.dayOfWeek = r.dayOfWeek;
        this.cat = r.cat;
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.exercises = r.exercises;
    }

    public int getID()                                      { return this.ID; }
    public int getUserID()                                  { return this.userID; }
    public int getDayOfWeek()                               { return this.dayOfWeek; }
    public int getCat()                                     { return this.cat; }
    public String getStartTime()                            { return this.startTime; }
    public String getEndTime()                              { return this.endTime; }
    public ArrayList<ExerciseData> getExercises()           { return (ArrayList<ExerciseData>) this.exercises; }

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

    public void setID(int ID)                               { this.ID = ID; }
    public void setUserID(int userID)                       { this.userID = userID; }
    public void setDayOfWeek(int dayOfWeek)                 { this.dayOfWeek = dayOfWeek; }
    public void setCat(int cat)                             { this.cat = cat; }
    public void setStartTime(String startTime)              { this.startTime = startTime; }
    public void setEndTime(String endTime)                  { this.endTime = endTime; }
    public void setExercises(ArrayList<ExerciseData> exercises)  { this.exercises = exercises; }

}
