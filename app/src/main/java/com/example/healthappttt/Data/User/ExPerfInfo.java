package com.example.healthappttt.Data.User;

import com.google.gson.annotations.SerializedName;

public class ExPerfInfo {

@SerializedName("benchValue")
private int benchValue;

@SerializedName("squatValue")
private int squatValue;

@SerializedName("deadliftValue")
private int deadliftValue;

public ExPerfInfo(int benchValue, int squatValue, int deadliftValue) {
        this.benchValue = benchValue;
        this.squatValue = squatValue;
        this.deadliftValue = deadliftValue;
        }

public int getBenchValue() {
        return benchValue;
        }

public void setBenchValue(int benchValue) {
        this.benchValue = benchValue;
        }

public int getSquatValue() {
        return squatValue;
        }

public void setSquatValue(int squatValue) {
        this.squatValue = squatValue;
        }

public int getDeadliftValue() {
        return deadliftValue;
        }

public void setDeadliftValue(int deadliftValue) {
        this.deadliftValue = deadliftValue;
        }
        }
