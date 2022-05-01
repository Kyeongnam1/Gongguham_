package com.example.gongguham_;

public class PostInfo {
    private String postTitle;
    private String postContent;
    private String meetingArea;
    private String closeTime;
    private int maxPerson;
    private String userId;

    public PostInfo(String postTitle, String postContent, String meetingArea, String closeTime, int maxPerson, String userId) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.meetingArea = meetingArea;
        this.closeTime = closeTime;
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

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
