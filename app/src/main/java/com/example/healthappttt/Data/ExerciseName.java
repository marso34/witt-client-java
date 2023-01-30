package com.example.healthappttt.Data;

import java.io.Serializable;

public class ExerciseName implements Serializable {
    String Name;
    String Cat;

    public ExerciseName(String Name, String Cat) {
        this.Name = Name;
        this.Cat = Cat;
    }

    public ExerciseName(ExerciseName e) {
        this.Name = e.Name;
        this.Cat = e.Cat;
    }


    public void setName(String name) { this.Name = name; }
    public String getName()          { return this.Name; }
    public void setCat(String cat)   { this.Cat = cat; }
    public String getCat()           { return this.Cat; }
}
