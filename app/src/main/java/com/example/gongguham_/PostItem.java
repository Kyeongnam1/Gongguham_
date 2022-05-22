package com.example.gongguham_;

import android.os.Parcel;
import android.os.Parcelable;

public class PostItem implements Parcelable {
    String postTitle;
    String meetingArea;
    String closeTime;
    int maxPerson;
    int postImg;



    int postIdx;


    public PostItem(String postTitle, String meetingArea, String closeTime, int maxPerson, int postIdx){
        this.postTitle = postTitle;
        this.meetingArea = meetingArea;
        this.closeTime = closeTime;
        this.maxPerson = maxPerson;
        this.postIdx = postIdx;

    }

    protected PostItem(Parcel in) {
        postTitle = in.readString();
        meetingArea = in.readString();
        closeTime = in.readString();
        maxPerson = in.readInt();
        postIdx = in.readInt();
    }

    public static final Creator<PostItem> CREATOR = new Creator<PostItem>() {
        @Override
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }

        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setMeetingArea(String meetingArea) {
        this.meetingArea = meetingArea;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public void setPostIdx(int postIdx) {
        this.postIdx = postIdx;
    }

    public void setPostImg(int postImg) {
        this.postImg = postImg;
    }

    public int getPostImg() {
        return postImg;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getMeetingArea() {
        return meetingArea;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public int getPostIdx() {
        return postIdx;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postTitle);
        parcel.writeString(meetingArea);
        parcel.writeString(closeTime);
        parcel.writeInt(maxPerson);
        parcel.writeInt(postIdx);
    }
}
