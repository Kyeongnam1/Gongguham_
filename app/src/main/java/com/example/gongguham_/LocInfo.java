package com.example.gongguham_;

public class LocInfo {

    private String curLoc;

    LocInfo(String curLoc){
        this.curLoc = curLoc;
    }


    public String getCurLoc(){
        return this.curLoc;
    }
    public void setCurLoc(String curLoc){
        this.curLoc = curLoc;
    }
}