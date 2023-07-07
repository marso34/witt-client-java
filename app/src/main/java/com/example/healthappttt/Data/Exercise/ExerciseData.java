package com.example.healthappttt.Data.Exercise;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExerciseData implements Serializable {
    @SerializedName("ID")
    private int ID;
    @SerializedName("parentID")
    private int parentID;

    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("cat")
    private int cat;

    @SerializedName("setOrTime")
    private int setOrTime;

    @SerializedName("volume")
    private int volume;

    @SerializedName("cntOrDis")
    private int cntOrDis;

    @SerializedName("index")
    private int index;

    public ExerciseData(String exerciseName, int cat) {
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = 0;      // 세트 카운트 or 시간(유산소)
        this.volume = 0;    // 무게 or 속도 (유산소)
        this.cntOrDis = 0;
    }
    public ExerciseData(int parentID, String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.parentID = parentID;
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }

    public ExerciseData(int ID, int parentID, String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.ID = ID;
        this.parentID = parentID;
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }


    public ExerciseData(String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }

    public ExerciseData(ExerciseData e) {
        this.ID = e.ID;
        this.parentID = e.parentID;
        this.exerciseName = e.exerciseName;
        this.cat = e.cat;
        this.setOrTime = e.setOrTime;
        this.volume = e.volume;
        this.cntOrDis = e.cntOrDis;
        this.index = e.index;
    }

    public int getID()                      { return this.ID; }
    public int getParentID()                { return this.parentID; }
    public String getExerciseName()         { return this.exerciseName; }
    public int getCat()                     { return this.cat; }
    public int getSetOrTime()               { return this.setOrTime; }
    public int getVolume()                  { return this.volume; }
    public int getCntOrDis()                { return this.cntOrDis; }
    public int getIndex()                   { return this.index; }

    public String getStrCat() { // 나중에 getStrCat으로 변경
        switch (this.cat) {
            case 0x1:  return "가슴";
            case 0x2:  return "등";
            case 0x4:  return "어깨";
            case 0x8:  return "하체";
            case 0x10: return "팔";
            case 0x20: return "복근";
            case 0x40: return "유산소";
        }

        return null;
    }
    public String getColor() {
        switch (this.cat) {
            case 0x1:  return "#eee6fa";
            case 0x2:  return "#d9f7ee";
            case 0x4:  return "#fde6f3";
            case 0x8:  return "#ffefeb";
            case 0x10: return "#fef8ee";
            case 0x20: return "#f9e6eb";
            case 0x40: return "#e6f1fd";
        }

        return null;
    }
    public String getTextColor() {
        switch (this.cat) {
            case 0x1:  return "#8C5ADB";
            case 0x2:  return "#05C78C";
            case 0x4:  return "#F257AF";
            case 0x8:  return "#FC673F";
            case 0x10: return "#F2BB57";
            case 0x20: return "#C71040";
            case 0x40: return "#579EF2";
        }

        return null;
    }

    public void setID(int ID)               { this.ID = ID; }
    public void setParentID(int parentID)   { this.parentID = parentID; }
    public void setSetOrTime(int setOrTime) { this.setOrTime = setOrTime; }
    public void setVolume(int volume)       { this.volume = volume; }
    public void setCntOrDis(int cntOrDis)   { this.cntOrDis = cntOrDis; }
    public void setIndex(int index)         { this.index = index; }
}
