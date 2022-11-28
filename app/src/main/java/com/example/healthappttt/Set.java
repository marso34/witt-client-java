package com.example.healthappttt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Set implements Serializable {
    private String weight;
    private String count;
    private String time;

    public Set(String weight, String count, String time) {
        this.weight = weight;
        this.count = count;
        this.time = time;
    }

    public Set(Set set) {
        this.weight = set.getWeight();
        this.count = set.getCount();
        this.time = set.getTime();
    }

    public Map<String, Object> getSet() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("weight", weight);
        docData.put("count", count);
        docData.put("time",time);

        return  docData;
    }

    public String getWeight(){
        return this.weight;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public String getCount() { return this.count; }
    public void setCount(String count) { this.count = count; }
    public String getTime() { return this.time; }
    public void setTime(String time) { this.time = time; }
}
