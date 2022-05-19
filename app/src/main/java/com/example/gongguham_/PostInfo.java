package com.example.gongguham_;

public class PostInfo {
    private String postTitle;
    private String postContent;
    private String meetingArea;
    private String closeTime_hour;
    private String closeTime_minute;
    private int maxPerson;

    //채팅방명 추가
    private String ChatTitle;

    private String userLocation;

    public PostInfo(String postTitle, String postContent, String meetingArea, String closeTime_hour, String closeTime_minute, int maxPerson, String userLocation, String ChatTitle) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.meetingArea = meetingArea;
        this.closeTime_hour = closeTime_hour;
        this.closeTime_minute = closeTime_minute;
        this.maxPerson = maxPerson;
        this.userLocation = userLocation;
        this.ChatTitle = ChatTitle;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getMeetingArea() {
        return meetingArea;
    }

    public void setMeetingArea(String meetingArea) {
        this.meetingArea = meetingArea;
    }

    public String getCloseTime_hour() {
        return closeTime_hour;
    }

    public void setCloseTime_hour(String closeTime_hour) {
        this.closeTime_hour = closeTime_hour;
    }

    public String getCloseTime_minute() {
        return closeTime_minute;
    }

    public void setCloseTime_minute(String closeTime_minute) { this.closeTime_minute = closeTime_minute; }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    // 채팅방 이름 관련 추가
    public String getChatTitle() {
        return ChatTitle;
    }

    public void setChatTitle(String ChatTitle) { this.ChatTitle = ChatTitle; }
}