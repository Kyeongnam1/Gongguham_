package com.example.gongguham_;

public class PostInfo {
    private String postTitle;
    private String postCategory;
    private String postContent;
    private String meetingArea;
    private String closeTime_hour;
    private String closeTime_minute;
    private int maxPerson;
    private int deliveryFee;
    private String postEmail;
    private int curPerson;

    //채팅방명 추가
    private String ChatTitle;

    private String userLocation;

    public PostInfo(String postTitle, String postCategory, String postContent, String meetingArea, String closeTime_hour, String closeTime_minute, int maxPerson, int deliveryFee, String userLocation, String ChatTitle, String postEmail, int curPerson) {
        this.postTitle = postTitle;
        this.postCategory = postCategory;
        this.postContent = postContent;
        this.meetingArea = meetingArea;
        this.closeTime_hour = closeTime_hour;
        this.closeTime_minute = closeTime_minute;
        this.maxPerson = maxPerson;
        this.deliveryFee = deliveryFee;
        this.userLocation = userLocation;
        this.ChatTitle = ChatTitle;
        this.postEmail = postEmail;
        this.curPerson = curPerson;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
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

    public void setMaxPerson(int curPerson) {
        this.maxPerson = maxPerson;
    }

    public int getCurPerson() {
        return curPerson;
    }

    public void setCurPerson(int curPerson) {
        this.curPerson = curPerson;
    }

    // 채팅방 이름 관련 추가
    public String getChatTitle() {
        return ChatTitle;
    }

    public void setChatTitle(String ChatTitle) { this.ChatTitle = ChatTitle; }

    public String getpostEmail() {
        return postEmail;
    }

    public void setpostEmail(String postEmail) { this.postEmail = postEmail; }
}