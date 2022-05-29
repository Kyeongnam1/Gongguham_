package com.example.gongguham_;

public class MemberInfo {
    private String name;
    private String phoneNumber;
    private String gender;
    private String accountValue;
    private String account;
    private String birthday;
    private int point;
    private int ReviewTotalScore;
    private int ReviewAvScore;
    private int ReviewNumber;
    private String curPost;

    MemberInfo(String name, String phoneNumber, String gender,String accountValue, String account, String birthday, int point, int ReviewTotalScore, int ReviewAvScore, int ReviewNumber, String curPost){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.accountValue = accountValue;
        this.account = account;
        this.birthday = birthday;
        this.point = point;
        this.ReviewTotalScore = ReviewTotalScore;
        this.ReviewAvScore = ReviewAvScore;
        this.ReviewNumber = ReviewNumber;
        this.curPost = curPost;
    }

    public int getReviewNumber() {
        return ReviewNumber;
    }

    public void setReviewNumber(int reviewNumber) {
        ReviewNumber = reviewNumber;
    }

    public int getReviewAvScore() {
        return ReviewAvScore;
    }

    public void setReviewAvScore(int reviewAvScore) {
        ReviewAvScore = reviewAvScore;
    }

    public int getReviewTotalScore() {
        return ReviewTotalScore;
    }

    public void setReviewTotalScore(int reviewTotalScore) {
        ReviewTotalScore = reviewTotalScore;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }

    public String getAccountValue(){
        return this.accountValue;
    }
    public void setAccountValue(String accountValue){
        this.accountValue = accountValue;
    }

    public String getAccount(){
        return this.account;
    }
    public void setAccount(String account){
        this.account = account;
    }

    public String getBirthday(){
        return this.birthday;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public int getPoint(){ return this.point;}
    public void setPoint(int point){ this.point = point; }

    public String getCurPost(){
        return this.curPost;
    }
    public void setCurPost(String curPost){
        this.curPost = curPost;
    }
}
