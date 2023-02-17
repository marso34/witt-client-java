package com.example.healthappttt.Data;

public class CompareUser implements Comparable<CompareUser>{
    private User U;
    private Integer distance;

    public CompareUser(User U, Integer D){
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

    public Integer getDistance(){
        return this.distance;
    }
    public User getUser(){
        return this.U;
    }
}
