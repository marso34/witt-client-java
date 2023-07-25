package com.example.healthappttt.Data.User;

public class LocData {
    private String name;
    private String id;

    private String adress;


    public LocData(String name, String id, String adress) {
        this.name = name;
        this.id = id;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAdress() {
        return adress;
    }
}