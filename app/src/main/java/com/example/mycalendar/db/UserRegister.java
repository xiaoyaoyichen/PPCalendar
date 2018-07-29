package com.example.mycalendar.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 逍遥依尘 on 2018/5/15.
 */

public class UserRegister  {
    private String UserId;
    private String Password;
    private String UserName;
    private String UserBirth;

    public String getUserId(){
        return UserId;
    }

    public void setUserId(String UserId){
        this.UserId=UserId;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(String Password){
        this.Password=Password;
    }

    public String getUserBirth() {
        return UserBirth;
    }

    public void setUserBirth(String UserBirth) {
        this.UserBirth = UserBirth;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String toString(){
        return "UserRegister [UserId=" + UserId + ",UserName=" + UserName + ",UserBirth=" + UserBirth + ",Password=" + Password + "]";
    }
}
