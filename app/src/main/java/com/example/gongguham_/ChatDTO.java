package com.example.gongguham_;

public class ChatDTO {

    private String userName;
    private String message;
    private String time;

    public ChatDTO() {}
    public ChatDTO(String userName, String message, String time) {
        this.userName = userName;
        this.message = message;
        this.time = time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String message) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
