package com.example.gongguham_;

public class LocInfo {

    private String curLoc;
    //private String email;

    LocInfo(String curLoc){
        this.curLoc = curLoc;
    }


    public String getCurLoc(){
        return this.curLoc;
    }
    public void setCurLoc(String curLoc){
        this.curLoc = curLoc;
    }
    //public String getEmail(){ return this.email; }
    //public void setEmail(String email){this.email = email;}
}