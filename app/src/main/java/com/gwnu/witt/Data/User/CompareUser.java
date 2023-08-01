package com.gwnu.witt.Data.User;

import com.gwnu.witt.Data.UserInfo;

public class CompareUser implements Comparable<CompareUser>{
    private UserInfo U;
    private Integer distance;

    public CompareUser(UserInfo U, Integer D){
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
    public UserInfo getUser(){
        return this.U;
    }
}
