package com.example.gongguham_;

public class SenderHistoryInfo {
    private String email;
    private int plus_point;
    private int minus_point;
    private int current_point;
    private String Recipient;

    SenderHistoryInfo(String email, int plus_point, int minus_point, int current_point, String Recipient) {
        this.email = email;
        this.plus_point = plus_point;
        this.minus_point = minus_point;
        this.current_point = current_point;
        this.Recipient = Recipient;
    }

    public String getRecipient(){ return this.Recipient; }
    public void setRecipient(String Recipient){ this.Recipient = Recipient; }

    public String getEmail(){ return this.email; }
    public void setEmail(String email){ this.email = email; }

    public int getCurrent_point(){ return this.current_point; }
    public void setCurrent_point(int current_point){ this.current_point = current_point; }

    public int getMinus_point(){ return this.minus_point; }
    public void setMinus_point(int minus_point){ this.minus_point = minus_point; }

    public int getPlus_point(){ return this.plus_point; }
    public void setPlus_point(int plus_point){ this.plus_point = plus_point; }
}
