package com.example.healthappttt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Routine implements Serializable { // routine 으로 수정
    private String title;
    private String startTime;
    private String endTime;
    private String runTime;
    private String exercizeCategories; // exercizeCategories 수정
    private String notes;

    private ArrayList<Exercize> exercizes;

    public Routine(String title, String exerciseArea) {
        this.title = title;
        this.exercizeCategories = exerciseArea;
        this.exercizes = new ArrayList<Exercize>();
    }

    public Routine(String title, String exerciseArea, ArrayList<Exercize> exercizes) {
        this.title = title;
        this.exercizeCategories = exerciseArea;
        this.exercizes = new ArrayList<Exercize>(exercizes);
    }

    public Routine(Routine r) {
        this.title = r.title;
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.runTime = r.runTime;
        this.exercizeCategories = r.exercizeCategories;
        this.exercizes = new ArrayList<Exercize>(r.exercizes);
    }


    public Map<String, Object> getRoutine() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("runTime", runTime);
        docData.put("exercizeCategories",exercizeCategories);
        docData.put("exercizes", exercizes);

        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getStartTime() { return this.startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return this.endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getRunTime() { return this.runTime; }
    public void setRunTime(String runTime) { this.runTime = runTime; }
    public String getExercizeCategories() { return this.exercizeCategories; }
    public void setExercizeCategories(String exercizeCategories) { this.exercizeCategories = exercizeCategories; }
    public String getNotes() { return this.notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public ArrayList<Exercize> getExercizes() { return this.exercizes; }
    public void setExercizes(ArrayList<Exercize> exercizes) { this.exercizes = new ArrayList<Exercize>(exercizes); }
    public int getExerciezeCount() { return this.exercizes.size(); }
}



