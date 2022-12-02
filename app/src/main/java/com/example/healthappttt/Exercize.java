package com.example.healthappttt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercize implements Serializable {
    private String title;
    private String state; // 무게만 or 무게 세트수 or 시간
    private String color;
    private String startTime;
    private String endTime;

    private ArrayList<Set> exercizeSet; // 나중에 ArrayList로 변경

    public Exercize(String title, String state, ArrayList<Set> exercizeSet) {
        this.title = title;
        this.state = state;
        this.color = "기본색"; // 기본색
        this.exercizeSet = new ArrayList<>(exercizeSet);
    }

    public Exercize(String title, String state, String color, ArrayList<Set> exercizeSet) {
        this.title = title;
        this.state = state;
        this.color = color;
        this.exercizeSet = new ArrayList<>(exercizeSet);
    }

    public Exercize(Exercize e) {
        this.title = e.title;
        this.state = e.state;
        this.color = e.color;
        this.startTime = e.startTime;
        this.endTime = e.endTime;

        this.exercizeSet = new ArrayList<>(e.exercizeSet);
    }

    public Map<String, Object> getExercize() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("state", state);
        docData.put("color",color);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("exercizeSet", exercizeSet);

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
    public String getColor() { return this.color; }
    public void setColor(String color) { this.color = color; }
    public String getStartTime() { return this.startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return this.endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public ArrayList<Set> getExercizeSet() { return this.exercizeSet; }
    public void setExercizeSet(ArrayList<Set> exercizeSet) { this.exercizeSet = new ArrayList<>(exercizeSet); }
    public int getExercizeSetCount() { return exercizeSet.size(); }
}