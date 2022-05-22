package com.example.gongguham_;

public class FriendsInfo {
    private String email;
    private String name;

    FriendsInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail(){ return this.email; }
    public void setEmail(String email){ this.email = email; }

    public String getName(){ return this.name; }
    public void setName(String name){ this.name = name; }
}
