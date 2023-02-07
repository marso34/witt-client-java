package com.example.healthappttt.Data;

import java.io.Serializable;

public class ExerciseName implements Serializable {
    String Name;
    int Cat;

    public ExerciseName(String Name, int Cat) {
        this.Name = Name;
        this.Cat = Cat;
    }

    public ExerciseName(ExerciseName e) {
        this.Name = e.Name;
        this.Cat = e.Cat;
    }


    public void setName(String name) { this.Name = name; }
    public String getName()          { return this.Name; }
    public void setCat(int cat)   { this.Cat = cat; }
    public int getCat()           { return this.Cat; }
    public String getStrCat()  {
        String str = "";

        switch(Cat) {
            case 0x1: str = "가슴"; break;
            case 0x2: str = "등"; break;
            case 0x4: str = "어깨"; break;
            case 0x8: str = "하체"; break;
            case 0x10: str = "팔"; break;
            case 0x20: str = "복근"; break;
            case 0x40: str = "유산소"; break;
        }

        return str;
    }
}
