package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routine implements Serializable {
    private String title;
    private String exerciseCategories;
    private String startTime;
    private String endTime;
    private String runTime;

    private ArrayList<Exercise> exercises;

    public Routine(String title, String exerciseCategories) {
        this.title = title;
        this.exerciseCategories = exerciseCategories;
        this.exercises = new ArrayList<Exercise>();
    }

    public Routine(String title, String exerciseCategories, ArrayList<Exercise> exercises) {
        this.title = title;
        this.exerciseCategories = exerciseCategories;
        this.exercises = new ArrayList<Exercise>(exercises);
    }

    public Routine(Routine r) {
        this.title = r.title;
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.runTime = r.runTime;
        this.exerciseCategories = r.exerciseCategories;
        this.exercises = new ArrayList<Exercise>(r.exercises);
    }

    public Map<String, Object> getRoutine() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("runTime", runTime);
        docData.put("exercizeCategories",exerciseCategories);
        docData.put("exercizes", exercises);

        return  docData;
    }

    public String getTitle()                                     { return this.title; }
    public void setTitle(String title)                           { this.title = title; }
    public String getStartTime()                                 { return this.startTime; }
    public void setStartTime(String startTime)                   { this.startTime = startTime; }
    public String getEndTime()                                   { return this.endTime; }
    public void setEndTime(String endTime)                       { this.endTime = endTime; }
    public String getRunTime()                                   { return this.runTime; }
    public void setRunTime(String runTime)                       { this.runTime = runTime; }
    public String getExerciseCategories()                        { return this.exerciseCategories; }
    public void setExerciseCategories(String exerciseCategories) { this.exerciseCategories = exerciseCategories; }
    public ArrayList<Exercise> getExercises()                    { return this.exercises; }
    public void setExercises(ArrayList<Exercise> exercises)      { this.exercises = new ArrayList<Exercise>(exercises); }
    public void addExercise(Exercise exercise)                   { this.exercises.add(exercise); }
    public int getExercieseCount()                               { return this.exercises.size(); }
}



