package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

    public class User implements Serializable {

        private String UserName = "";
        private Double UserTemperature = 36.5;
        private String ProfileImg = "";
        private String Bench = "";
        public String GoodTime = "";
        private String Deadlift = "";
        private String Squat = "";
        private String LocationName = "";
        private String Key ="";
        private Double Lat = 0.0;
        private Double Lon = 0.0;
        private Double Distance = 0.0;

//
//        public User(User a){
//            this.UserName = a.getUserName();
//            this.UserTemperature = a.getUserTemperature();
//            this.ProfileImg = a.getProfileImg();
//            this.Bench = a.getBench();
//            this.Deadlift = a.getDeadlift();
//            this.Squat = a.getSquat();
//            this.LocationName =a.getLocationName();
//            this.ReviewTableKey =  a.getReviewTableKey();// 유저생성할 때 이키로 유저만들것.
//            this.Lat = a.getLat();
//            this.Lon = a.getLon();
//            GoodTime = a.GoodTime;
//        }
        public User(String UID,String UN, String PRI, String Bench, String Deadlift, String Squat,String LCN) {
            this.UserName = UN;
            this.UserTemperature = 36.5;
            this.ProfileImg = PRI;
            this.Bench = Bench;
            this.Deadlift = Deadlift;
            this.Squat = Squat;
            this.LocationName = LCN;
            this.Key =  UID;// 유저생성할 때 이키로 유저만들것.
            this.Lat = 0.0;
            this.Lon = 0.0;
            GoodTime = "0";

        }
        public User(Double UserTemperature_,String ReviewTableKey_,Double lat,Double lon,String Good,String UN, String PRI, String Bench, String Deadlift, String Squat,String LCN) {
            this.UserName = UN;
            this.UserTemperature = UserTemperature_;
            this.ProfileImg = PRI;
            this.Bench = Bench;
            this.Deadlift = Deadlift;
            this.Squat = Squat;
            this.LocationName = LCN;
            this.Key = ReviewTableKey_;// 유저생성할 때 이키로 유저만들것.
            this.Lat = lat;
            this.Lon = lon;
            GoodTime = Good;
        }
        public String getUserName(){return this.UserName;}
        public String getBench(){
            return this.Bench;
        }
        public String getDeadlift(){
            return this.Deadlift;
        }
        public String getSquat(){
            return this.Squat;
        }

        public String getProfileImg(){
            return this.ProfileImg;
        }
        public Double getUserTemperature(){
            return this.UserTemperature;
        }
        public String getKey_(){
            return this.Key;
        }
        public String getLocationName(){return this.LocationName;}
        public Double getLat(){
            return this.Lat;
        }
        public Double getLon(){
            return  this.Lon;
        }
        public Double getDistance(){
            return this.Distance;
        }
        public void setDistance(Double D){
            this.Distance = D;
        }
        public void setLat(Double L){
            this.Lat = L;
        }

        public void setLon(Double L){
            this.Lon = L;
        }
    }


