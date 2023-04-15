package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise implements Serializable {
    private String title;
    private String state;  // 운동 부위 결정 (시간인지 무게, 세트인지)
    private String textColor;
    private String color;
    private int count;  // 세트 or 시간 (유산소일 때만 시간)
    private int volume; // 무게 or 속도 (유산소일 때만 속도) // 나중에 수정
//    private int ---; // 횟수
    private String startTime;
    private String endTime;

    public Exercise(String title, String state) {
        this.title = title;
        this.state = state;
        this.count = 5;      // 세트 카운트 or 시간(유산소)
        this.volume = 10;    // 무게 or 속도 (유산소)
        this.startTime = "";
        this.endTime = "";

        switch (state) {
            case "가슴" : this.color = "#eee6fa"; this.textColor = "#8C5ADB"; break; // 색은 일단 다 임시
            case "등"   : this.color = "#d9f7ee"; this.textColor = "#05C78C"; break;
            case "하체" : this.color = "#ffefeb"; this.textColor = "#FC673F"; break;
            case "어깨" : this.color = "#fde6f3"; this.textColor = "#F257AF"; break;
            case "복근" : this.color = "#f9e6eb"; this.textColor = "#C71040"; break;
            case "팔"   : this.color = "#fef8ee"; this.textColor = "#F2BB57"; break;
            case "유산소" : this.color = "#e6f1fd"; this.textColor = "#579EF2"; this.count = 3; this.volume = 8; break;
        }
    }

    public Exercise(String title, int state) {
        this.title = title;
        this.count = 5;      // 세트 카운트 or 시간(유산소)
        this.volume = 10;    // 무게 or 속도 (유산소)
        this.startTime = "";
        this.endTime = "";

        switch (state) {
            case 0x1:  this.state = "가슴"; this.color = "#eee6fa"; this.textColor = "#8C5ADB"; break;
            case 0x2:  this.state = "등"; this.color = "#d9f7ee"; this.textColor = "#05C78C"; break;
            case 0x4:  this.state = "어깨"; this.color = "#fde6f3"; this.textColor = "#F257AF"; break;
            case 0x8:  this.state = "하체"; this.color = "#ffefeb"; this.textColor = "#FC673F"; break;
            case 0x10: this.state = "팔"; this.color = "#fef8ee"; this.textColor = "#F2BB57"; break;
            case 0x20: this.state = "복근"; this.color = "#f9e6eb"; this.textColor = "#C71040"; break;
            case 0x40: this.state = "유산소"; this.color = "#e6f1fd"; this.textColor = "#579EF2"; this.count = 3; this.volume = 8; break;
        }
    }

    public Exercise(String title, String state, int count, int volume) {
        this.title = title;
        this.state = state;
        this.count = count;
        this.volume = volume;

        switch (state) {
            case "가슴" : this.color = "#eee6fa"; this.textColor = "#8C5ADB"; break; // 색은 일단 다 임시
            case "등"   : this.color = "#d9f7ee"; this.textColor = "#05C78C"; break;
            case "하체" : this.color = "#ffefeb"; this.textColor = "#FC673F"; break;
            case "어깨" : this.color = "#fde6f3"; this.textColor = "#F257AF"; break;
            case "복근" : this.color = "#f9e6eb"; this.textColor = "#C71040"; break;
            case "팔"   : this.color = "#fef8ee"; this.textColor = "#F2BB57"; break;
            case "유산소" : this.color = "#e6f1fd"; this.textColor = "#579EF2"; break;
        }
    }

    public Exercise(Exercise e) {
        this.title = e.title;
        this.state = e.state;
        this.color = e.color;
        this.count = e.count;
        this.volume = e.volume;
        this.startTime = e.startTime;
        this.endTime = e.endTime;
    }

    public String getTitle()                    { return this.title; }
    public String getState()                    { return this.state; }
    public String getColor()                    { return this.color; }
    public String getTextColor()                    { return this.textColor; }
    public int getCount()                       { return this.count; }
    public int getVolume()                      { return this.volume; }
    public String getStartTime()                { return this.startTime; }
    public String getEndTime()                  { return this.endTime; }
    public void setTitle(String title)          { this.title = title; }
    public void setState(String state)          { this.state = state; }
    public void setColor(String color)          { this.color = color; }
    public void setCount(int count)             { this.count = count; }
    public void setVolume(int volume)           { this.volume = volume; }
    public void setStartTime(String startTime)  { this.startTime = startTime; }
    public void setEndTime(String endTime)      { this.endTime = endTime; }
}