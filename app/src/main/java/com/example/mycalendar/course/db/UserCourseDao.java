package com.example.mycalendar.course.db;

/**
 * Created by 逍遥依尘 on 2018/6/28.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;

import java.util.LinkedList;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mycalendar.course.bean.CourseInfo;
import com.example.mycalendar.course.bean.UserCourse;

public class UserCourseDao {
    private DBHelper globalDBHelper;
    private SQLiteDatabase db;

    public UserCourseDao(Context context) {
        globalDBHelper = new DBHelper(context);
        db = globalDBHelper.getWritableDatabase();
    }

    public boolean insert(UserCourse courseData) {
        try {
            String sql = "INSERT INTO user_course (cid, uid) VALUES (?, ?)";
            db.execSQL(sql, new Object[] {
                    courseData.getCid(),
                    courseData.getUid()
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int  uid,int cid) {
        try {
            String sql = "DELETE FROM user_course WHERE cid=? AND uid=?";
            db.execSQL(sql, new Object[] {
                    cid,uid
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clear(int uid) {
        try {
            String sql = "DELETE FROM user_course WHERE uid=?";
            db.execSQL(sql, new Object[] {uid});
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // 查询某用户的所有课程
    public LinkedList<CourseInfo> query(int uid) {
        try {
            LinkedList<CourseInfo> cInfos = new LinkedList<CourseInfo>();

            String sql = "SELECT * FROM course_info WHERE cid IN (SELECT cid FROM user_course WHERE uid=?)";
            Cursor c = db.rawQuery(sql, new String[]{String.valueOf(uid)});

            while (c.moveToNext()) {
                CourseInfo cInfo = new CourseInfo();
                cInfo.setCid(c.getInt(c.getColumnIndex("cid")));
                cInfo.setWeekfrom(c.getInt(c.getColumnIndex("weekfrom")));
                cInfo.setWeekto(c.getInt(c.getColumnIndex("weekto")));
                cInfo.setWeektype(c.getInt(c.getColumnIndex("weektype")));
                cInfo.setDay(c.getInt(c.getColumnIndex("day")));
                cInfo.setLessonfrom(c.getInt(c.getColumnIndex("lessonfrom")));
                cInfo.setLessonto(c.getInt(c.getColumnIndex("lessonto")));
                cInfo.setCoursename(c.getString(c.getColumnIndex("coursename")));
                cInfo.setTeacher(c.getString(c.getColumnIndex("teacher")));
                cInfo.setPlace(c.getString(c.getColumnIndex("place")));
                cInfos.add(cInfo);
            }

            return cInfos;
        } catch (Exception e) {
            return null;
        }

    }
}

