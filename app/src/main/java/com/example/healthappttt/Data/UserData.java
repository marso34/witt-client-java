package com.example.healthappttt.Data;

public class UserData {
    private String name;
    private String email;

    public UserData(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}