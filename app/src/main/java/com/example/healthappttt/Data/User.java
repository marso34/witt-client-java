package com.example.healthappttt.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

    public class User implements Serializable {
        private String UserId;
        private String UserPwd;
        private String UserName;
        private Double UserTemperature;
        private String ProfileImg;
        private String Bench;
        public String GoodTime;
        private String Deadlift;
        private String Squat;
        private String LocationName;
        private String ReviewTableKey;
        private Double Lat;
        private Double Lon;

        public User(String UID, String UPD,String UN, String PRI, String Bench, String Deadlift, String Squat,String LCN) {
            this.UserId = UID;
            this.UserPwd = UPD;
            this.UserName = UN;
            this.UserTemperature = 36.5;
            this.ProfileImg = PRI;
            this.Bench = Bench;
            this.Deadlift = Deadlift;
            this.Squat = Squat;
            this.LocationName = LCN;
            this.ReviewTableKey =  UID + UPD + Calendar.getInstance().getTime();// 유저생성할 때 이키로 유저만들것.
            this.Lat = 0.0;
            this.Lon = 0.0;
            GoodTime = "0";
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
        public String getGoodTime(){
            return this.GoodTime;
        }
        public String getProfileImg(){
            return this.ProfileImg;
        }
        public Double getUserTemperature(){
            return this.UserTemperature;
        }
        public String getReviewTableKey(){
            return this.ReviewTableKey;
        }
        public String getLocationName(){return this.LocationName;}
        public Double getLat(){
            return this.Lat;
        }
        public Double getLon(){
            return  this.Lon;
        }

    }


