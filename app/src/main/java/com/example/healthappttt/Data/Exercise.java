package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise implements Serializable {
    private int ID;
    private int parentID;
    private String title;
    private int cat;  // 운동 부위 결정 (시간인지 무게, 세트인지)
    private int count;  // 세트 or 시간 (유산소일 때만 시간)
    private int volume; // 무게 or 속도 (유산소일 때만 속도) // 나중에 수정
    private int num; // 횟수
    private int index;

    public Exercise(String title, int cat) {
        this.title = title;
        this.cat = cat;
        this.count = 0;      // 세트 카운트 or 시간(유산소)
        this.volume = 0;    // 무게 or 속도 (유산소)
        this.num = 0;
    }

    public Exercise(int ID, int parentID, String title, int cat, int index) {
        this.ID = ID;
        this.parentID = parentID;
        this.title = title;
        this.cat = cat;
        this.count = 0;      // 세트 카운트 or 시간(유산소)
        this.volume = 0; // 무게 or 속도 (유산소)
        this.num = 0;
        this.index = index;
    }

    public Exercise(String title, int cat, int count, int volume, int num, int index) {
        this.title = title;
        this.cat = cat;
        this.count = count;
        this.volume = volume;
        this.num = num;
        this.index = index;
    }

    public Exercise(int ID, int parentID, String title, int cat, int count, int volume, int num, int index) {
        this.ID = ID;
        this.parentID = parentID;
        this.title = title;
        this.cat = cat;
        this.count = count;
        this.volume = volume;
        this.num = num;
        this.index = index;
    }

    public Exercise(Exercise e) {
        this.ID = e.ID;
        this.parentID = e.parentID;
        this.title = e.title;
        this.cat = e.cat;
        this.count = e.count;
        this.volume = e.volume;
        this.num = e.num;
        this.index = e.index;
    }

    public int getID()                      { return this.ID; }
    public int getParentID()               { return this.parentID; }
    public String getTitle()                { return this.title; }
    public int getCat()                     { return this.cat; }
    public int getCount()                   { return this.count; }
    public int getVolume()                  { return this.volume; }
    public int getNum()                     { return this.num; }
    public int getIndex()                   { return this.index; }

    public String getState() { // 나중에 getStrCat으로 변경
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
    public void setParentID(int parentID) { this.parentID = parentID; }
    public void setCount(int count)         { this.count = count; }
    public void setVolume(int volume)       { this.volume = volume; }
    public void setNum(int num)             { this.num = num; }
    public void setIndex(int index)         { this.index = index; }
}