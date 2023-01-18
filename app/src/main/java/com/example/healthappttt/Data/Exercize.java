package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercize implements Serializable {
    private String title;
    private String state; // 무게, 세트수 or 시간을 결정. ex) 1이면 무게 및 세트, 2면 시간
    private String count; // 무게, 세트를 or 운동 시간 카운트
    private String color;
    private String startTime;
    private String endTime;

//    private ArrayList<Set> exercizeSet; // 나중에 ArrayList로 변경

    public Exercize(String title, String state, String count) {
        this.title = title;
        this.state = state;
        this.count = count;
        this.color = "기본색"; // 기본색
    }

    public Exercize(String title, String state, String count, String color) {
        this.title = title;
        this.state = state;
        this.count = count;
        this.color = color;
    }

    public Exercize(Exercize e) {
        this.title = e.title;
        this.state = e.state;
        this.count = e.count;
        this.color = e.color;
        this.startTime = e.startTime;
        this.endTime = e.endTime;

//        this.exercizeSet = new ArrayList<>(e.exercizeSet);
    }

    public Map<String, Object> getExercize() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("state", state);
        docData.put("color",color);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
//        docData.put("exercizeSet", exercizeSet);

        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getState() { return this.state; }
    public void setState(String state) { this.state = state; }
    public String getCount() { return this.count; }
    public void setCount(String count) { this.count = count; }
    public String getColor() { return this.color; }
    public void setColor(String color) { this.color = color; }
    public String getStartTime() { return this.startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return this.endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
//    public ArrayList<Set> getExercizeSet() { return this.exercizeSet; }
//    public void setExercizeSet(ArrayList<Set> exercizeSet) { this.exercizeSet = new ArrayList<>(exercizeSet); }
    public int getExercizeSetCount() { return 1; } // count를 리턴하는데 숫자로 변환해서 반환
}