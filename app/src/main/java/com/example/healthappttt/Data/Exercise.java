package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise implements Serializable {
    private String title;
    private String state;  // 운동 부위 결정 (시간인지 무게, 세트인지)
    private String color;
    private int count;  // 세트 or 시간 (유산소일 때만 시간)
    private int volume; // 무게 or 속도 (유산소일 때만 속도)
    private String startTime;
    private String endTime;

    public Exercise(String title, String state, int count, int volume) {
        this.title = title;
        this.state = state;
        this.count = count;
        this.volume = volume;

        switch (state) {
            case "가슴" : this.color = "#f257af"; break;
            case "등" : this.color = "#e26e5b"; break;
            case "하체" : this.color = "#05c78c"; break;
            case "어깨" : this.color = "#8c5adb"; break;
            case "유산소" : this.color = "#579ef2"; break;
        }
    }

    public Exercise(Exercise e) {
        this.title = e.title;
        this.state = e.state;
        this.count = e.count;
        this.startTime = e.startTime;
        this.endTime = e.endTime;
    }

    public Map<String, Object> getExercise() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("state", state);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        return  docData;
    }

    public String getTitle()                    { return this.title; }
    public void setTitle(String title)          { this.title = title; }
    public String getState()                    { return this.state; }
    public void setState(String state)          { this.state = state; }
    public String getColor()                    { return this.color; }
    public void setColor(String color)          { this.color = color; }
    public int getCount()                       { return this.count; }
    public void setCount(int count)             { this.count = count; }
    public int getVolume()                      { return this.volume; }
    public void setVolume(int volume)           { this.volume = volume; }
    public String getStartTime()                { return this.startTime; }
    public void setStartTime(String startTime)  { this.startTime = startTime; }
    public String getEndTime()                  { return this.endTime; }
    public void setEndTime(String endTime)      { this.endTime = endTime; }
}