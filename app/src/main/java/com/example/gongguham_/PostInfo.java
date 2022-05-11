package com.example.gongguham_;

public class PostInfo {
    private String postTitle;
    private String postContent;
    private String meetingArea;
    private String closeTime_hour;
    private String closeTime_minute;
    private String maxPerson;
    private String userId;

    public PostInfo(String postTitle, String postContent, String meetingArea, String closeTime_hour, String closeTime_minute, String maxPerson, String userId) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.meetingArea = meetingArea;
        this.closeTime_hour = closeTime_hour;
        this.closeTime_minute = closeTime_minute;
        this.maxPerson = maxPerson;
        this.userId = userId;
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

    public String getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(String maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
<<<<<<< HEAD
}
