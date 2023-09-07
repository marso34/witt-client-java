package com.gwnu.witt.Data.Exercise;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutineAiData extends RoutineData {
    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("bench")
    private int bench;

    @SerializedName("deadlift")
    private int deadlift;

    @SerializedName("squat")
    private int squat;

    public RoutineAiData(RoutineData r, int height, int weight, int bench, int deadlift, int squat) {
        super(r);
        this.height = height;
        this.weight = weight;
        this.bench = bench;
        this.deadlift = deadlift;
        this.squat = squat;
    }

    public int getHeight()   { return this.height; }

    public int getWeight()   { return weight; }

    public int getBench()    { return bench; }

    public int getDeadlift() { return deadlift; }

    public int getSquat()    { return squat; }


    public void setHeight(int height)     { this.height = height; }

    public void setWeight(int weight)     { this.weight = weight; }

    public void setBench(int bench)       { this.bench = bench; }

    public void setDeadlift(int deadlift) { this.deadlift = deadlift; }

    public void setSquat(int squat)       { this.squat = squat; }
}
