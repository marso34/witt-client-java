package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routine implements Serializable {
    private String title;
    private int exerciseCategories;
    private String startTime;
    private String endTime;
    private String runTime;

    private ArrayList<Exercise> exercises;

    public Routine(String title, int exerciseCategories) {
        this.title = title;
        this.exerciseCategories = exerciseCategories;
        this.exercises = new ArrayList<Exercise>();
        this.startTime = "00:00";
        this.endTime = "00:00";
        this.runTime = "0";
    }

    public Routine(String title, int exerciseCategories, String startTime, String endTime) {
        this.title = title;
        this.exerciseCategories = exerciseCategories;
        this.exercises = new ArrayList<Exercise>();
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = "0";
    }

    public Routine(String title, int exerciseCategories, ArrayList<Exercise> exercises) {
        this.title = title;
        this.exerciseCategories = exerciseCategories;
        this.exercises = new ArrayList<Exercise>(exercises);
        this.startTime = "00:00";
        this.endTime = "00:00";
        this.runTime = "0";
    }

    public Routine(Routine r) {
        this.title = r.title;
        this.exerciseCategories = r.exerciseCategories;
        this.exercises = new ArrayList<Exercise>(r.exercises);
        this.startTime = r.startTime;
        this.endTime = r.endTime;
        this.runTime = r.runTime;
    }

    public String getTitle()                                     { return this.title; }
    public String getStartTime()                                 { return this.startTime; }
    public String getEndTime()                                   { return this.endTime; }
    public String getRunTime()                                   { return this.runTime; }
    public int getExerciseCategories()                           { return this.exerciseCategories; }
    public ArrayList<Exercise> getExercises()                    { return this.exercises; }
    public int getExercieseCount()                               { return this.exercises.size(); }

    public void setTitle(String title)                           { this.title = title; }
    public void setStartTime(String startTime)                   { this.startTime = startTime; }
    public void setEndTime(String endTime)                       { this.endTime = endTime; }
    public void setRunTime(String runTime)                       { this.runTime = runTime; }
    public void setExerciseCategories(int exerciseCategories)    { this.exerciseCategories = exerciseCategories; }
    public void setExercises(ArrayList<Exercise> exercises)      { this.exercises = new ArrayList<Exercise>(exercises); }
    public void addExercise(Exercise exercise)                   { this.exercises.add(exercise); }
}



