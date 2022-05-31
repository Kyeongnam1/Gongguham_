package com.Gongguham.gongguham_;

public class UserInfo {
    private String name;
    private String accountValue;
    private String account;
    private int curPerson;

    UserInfo(String name, String accountValue, String account, int curPerson){
        this.curPerson = curPerson;
        this.name = name;
        this.accountValue = accountValue;
        this.account = account;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
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



    public int getCurPerson() {
        return curPerson;
    }

    public void setCurPerson(int curPerson) {
        this.curPerson = curPerson;
    }


}

