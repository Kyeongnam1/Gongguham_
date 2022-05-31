package com.Gongguham.gongguham_;

public class Applicant {

    String applicantName;
    String applicantPosition;
    String accountNumber;
    String bankName;
    String check;
    String foodName;
    String foodPrice;
    int curNum;
    public Applicant(String applicantName, String applicantPosition, String accountNumber, String bankName, String check, int curNum, String foodName, String foodPrice){
        this.applicantName = applicantName;
        this.applicantPosition = applicantPosition;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.check = check;
        this.curNum = curNum;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }


}
