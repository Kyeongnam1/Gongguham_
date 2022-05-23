package com.example.gongguham_;

public class RecipientHistoryInfo {
    private String email;
    private int plus_point;
    private int minus_point;
    private int current_point;
    private String sender;
    private String time;

    RecipientHistoryInfo(String email, int plus_point, int minus_point, int current_point, String sender, String time) {
        this.email = email;
        this.plus_point = plus_point;
        this.minus_point = minus_point;
        this.current_point = current_point;
        this.sender = sender;
        this.time = time;
    }

    public String getTime(){ return this.time; }
    public void setTime(String time){ this.time = time; }

    public String getSender(){ return this.sender; }
    public void setSender(String sender){ this.sender = sender; }

    public String getEmail(){ return this.email; }
    public void setEmail(String email){ this.email = email; }

    public int getCurrent_point(){ return this.current_point; }
    public void setCurrent_point(int current_point){ this.current_point = current_point; }

    public int getMinus_point(){ return this.minus_point; }
    public void setMinus_point(int minus_point){ this.minus_point = minus_point; }

    public int getPlus_point(){ return this.plus_point; }
    public void setPlus_point(int plus_point){ this.plus_point = plus_point; }
}
