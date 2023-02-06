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
        if (o.getDistance() < this.distance ) {
            return 1;
        }
        else if(o.getDistance() > this.distance){
            return -1;
        }
        else return 0;
    }

    public Double getDistance(){
        return this.distance;
    }
    public User getUser(){
        return this.U;
    }
}
