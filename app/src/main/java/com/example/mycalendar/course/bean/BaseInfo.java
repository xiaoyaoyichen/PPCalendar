package com.example.mycalendar.course.bean;

/**
 * Created by 逍遥依尘 on 2018/6/28.
 */

import java.io.Serializable;
import java.io.Serializable;

// BaseInfo用于记录整体信息
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = 2074656067805712769L;

    protected int weekfrom;	// 起始周
    protected int weekto;		// 结束周
    protected int weektype;	// 周类型：1普通；2单周；3双周
    protected int day;			// 星期几上课
    protected int lessonfrom;	// 开始节次
    protected int lessonto;	// 结束节次
    protected String place;	//地点

    public int getWeekfrom() {
        return weekfrom;
    }
    public void setWeekfrom(int weekfrom) {
        this.weekfrom = weekfrom;
    }
    public int getWeekto() {
        return weekto;
    }
    public void setWeekto(int weekto) {
        this.weekto = weekto;
    }
    public int getWeektype() {
        return weektype;
    }
    public void setWeektype(int weektype) {
        this.weektype = weektype;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getLessonfrom() {
        return lessonfrom;
    }
    public void setLessonfrom(int lessonfrom) {
        this.lessonfrom = lessonfrom;
    }
    public int getLessonto() {
        return lessonto;
    }
    public void setLessonto(int lessonto) {
        this.lessonto = lessonto;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }


    //仅测试使用
    public  BaseInfo() {
    }

    public BaseInfo (int weekfrom,int weekto, int weektype,int day,int lessonfrom,int lessonto,
                     String place){
        this.weekfrom = weekfrom;
        this.weekto = weekto;
        this.weektype = weektype;
        this.day = day;
        this.lessonfrom = lessonfrom;
        this.lessonto = lessonto;
        this.place=place;
    }

}
