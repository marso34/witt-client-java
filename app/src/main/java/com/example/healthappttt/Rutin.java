package com.example.healthappttt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Rutin implements Serializable {
    private String title;
    private String startTime;
    private String endTime;
    private String runTime;
    private String exerciseArea;
    private String notes;

    private ArrayList<Exercize> exercizes;

    public Rutin(String title, String exerciseArea) {
        this.title = title;
        this.exerciseArea = exerciseArea;
        this.exercizes = new ArrayList<Exercize>();
    }

    public Rutin(String title, String exerciseArea, ArrayList<Exercize> exercizes) {
        this.title = title;
        this.exerciseArea = exerciseArea;
        this.exercizes = new ArrayList<Exercize>(exercizes);
    }

    public Rutin(Rutin r) {
        this.title = r.title;
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.runTime = r.runTime;
        this.exerciseArea = r.exerciseArea;
        this.exercizes = new ArrayList<Exercize>(r.exercizes);
    }


    public Map<String, Object> getRutin() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("runTime", runTime);
        docData.put("exerciseArea",exerciseArea);
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
    public String getExerciseArea() { return this.exerciseArea; }
    public void setExerciseArea(String exerciseArea) { this.exerciseArea = exerciseArea; }
    public String getNotes() { return this.notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public ArrayList<Exercize> getExercizes() { return this.exercizes; }
    public void setExercizes(ArrayList<Exercize> exercizes) { this.exercizes = new ArrayList<Exercize>(exercizes); }
    public int getExerciezeCount() { return this.exercizes.size(); }
}



