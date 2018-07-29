package com.example.mycalendar.calendar;

/**
 * Created by 逍遥依尘 on 2018/7/1.
 */

public class DayFinish{
    public int day;
    public String lunar;
    boolean IsFestival;
    DayFinish(int day,String lunar,boolean IsFestival) {
        this.day = day;
        this.lunar = lunar;
        this.IsFestival = IsFestival;
    }
}
