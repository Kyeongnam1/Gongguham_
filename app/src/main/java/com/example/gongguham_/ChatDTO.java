package com.example.gongguham_;

public class ChatDTO {

    private String userName;
    private String message;
    private String time;
    private String password;

    public ChatDTO() {}
    
    public ChatDTO(String userName, String message, String time, String password) {
        this.userName = userName;
        this.message = message;
        this.time = time;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) { this.time = time; }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() { return time; }

    // 비밀번호 테스트
    public void setPassword(String password) { this.password = password;}
    public String getPassword() { return password; }

}
