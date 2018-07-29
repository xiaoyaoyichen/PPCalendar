package com.example.mycalendar.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by 逍遥依尘 on 2018/5/15.
 */

public class UserInfo extends DataSupport{
    private String UserId;
    private String UserName;
    private Date birthday;
    private String profilephoto;//用户头像路径
    private String backgroundimage;//背景图片路径

    public String getUserId(){
        return UserId;
    }

    public void setUserId(String UserId){
        this.UserId=UserId;
    }
    public String getUserName(){
        return UserName;
    }

    public void setUserName(String UserName){
        this.UserName=UserName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public void setBackgroundimage(String backgroundimage) {
        this.backgroundimage = backgroundimage;
    }
}
