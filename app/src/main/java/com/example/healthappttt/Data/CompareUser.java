package com.example.healthappttt.Data;

public class CompareUser implements Comparable<CompareUser>{
    private User U;
    private double distance;

    public CompareUser(User U, Double D){
        this.U = U;
        this.distance = D;
    }

    @Override
    public int compareTo(CompareUser o) {
        if (o.getDistance() - getDistance() < 2.0) {
            return 1;
        }
        else {
            return (int)(o.getDistance() - getDistance());
        }
    }

    public Double getDistance(){
        return this.distance;
    }
    public User getUser(){
        return this.U;
    }
}
