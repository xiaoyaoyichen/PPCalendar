package com.example.mycalendar.course;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycalendar.MainActivity;
import com.example.mycalendar.R;
import com.example.mycalendar.course.bean.CourseInfo;
import com.example.mycalendar.course.bean.GlobalInfo;
import com.example.mycalendar.course.bean.UserCourse;
import com.example.mycalendar.course.bean.UserInfo;
import com.example.mycalendar.course.db.CourseInfoDao;
import com.example.mycalendar.course.db.GlobalInfoDao;
import com.example.mycalendar.course.db.UserCourseDao;
import com.example.mycalendar.course.db.UserInfoDao;

import java.util.LinkedList;

public class CourseAddActivity extends AppCompatActivity {

    private static Context context;
    GlobalInfoDao gInfoDao;
    UserInfoDao uInfoDao;

    CourseInfoDao cInfoDao;
    UserCourseDao uCourseDao;
    GlobalInfo gInfo;//需要isFirstUse和activeUserUid
    UserInfo uInfo;//需要username昵称,gender，phone，headshot，institute，major，year
    private int uid;
    private int cid;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private View backView;

    private EditText courseNameEdit;
    private EditText courseTeacherEdit;
    private EditText courseBeginTimeEdit;
    private EditText courseEndTimeEdit;
    private EditText courseBeginWeekEdit;
    private EditText courseEndWeekEdit;
    private EditText coursePlaceEdit;
    private EditText courseWeekTimeEdit;
    private Button courseAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);
        init();
        courseAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseInfo cInfo = new CourseInfo();
                cInfo.setCid(cid);
                cInfo.setWeekfrom(Integer.parseInt(courseBeginWeekEdit.getText().toString()));
                cInfo.setWeekto(Integer.parseInt(courseEndWeekEdit.getText().toString()));
                cInfo.setWeektype(1);
                cInfo.setDay(Integer.parseInt(courseWeekTimeEdit.getText().toString()));
                cInfo.setLessonfrom(Integer.parseInt(courseBeginTimeEdit.getText().toString()));
                cInfo.setLessonto(Integer.parseInt(courseEndTimeEdit.getText().toString()));
                cInfo.setCoursename(courseNameEdit.getText().toString());
                cInfo.setTeacher(courseTeacherEdit.getText().toString());
                cInfo.setPlace(coursePlaceEdit.getText().toString());
                editor = pref.edit();
                editor.putInt("cid",cid+1);
                editor.apply();
                //如果添加成功，则插入数据库
                cInfoDao.insert(cInfo);
                UserCourse userCoursedata = new UserCourse();
                userCoursedata.setCid(cid);
                userCoursedata.setUid(uid);
                uCourseDao.insert(userCoursedata);
                Intent intent = new Intent(CourseAddActivity.this, MainActivity.class);
                intent.putExtra("fragmentId",3);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                finish();
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseAddActivity.this,MainActivity.class);
                intent.putExtra("fragmentId",3);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                finish();
            }
        });

    }

    private void init(){

        backView = findViewById(R.id.Btn_CourseAdd_Back);

        pref =  pref = PreferenceManager.getDefaultSharedPreferences(this);
        cid  = pref.getInt("cid",10);

        courseNameEdit = (EditText)findViewById(R.id.Add_Course_Name);
        coursePlaceEdit = (EditText)findViewById(R.id.Add_Course_Space);
        courseTeacherEdit = (EditText)findViewById(R.id.Add_Course_Teacher);
        courseBeginTimeEdit = (EditText)findViewById(R.id.Add_Course_beginTime);
        courseBeginWeekEdit = (EditText)findViewById(R.id.Add_Course_beginWeek);
        courseEndTimeEdit = (EditText)findViewById(R.id.Add_Course_endTime);
        courseEndWeekEdit = (EditText)findViewById(R.id.Add_Course_endWeek);
        courseWeekTimeEdit = (EditText)findViewById(R.id.Add_Course_WeekTime);
        courseAddBtn = (Button)findViewById(R.id.Btn_Add);

        // 初始化context
        context = getApplicationContext();

        // 初始化Dao成员变量
        gInfoDao = new GlobalInfoDao(context);
        uInfoDao = new UserInfoDao(context);

        cInfoDao = new CourseInfoDao(context);
        uCourseDao = new UserCourseDao(context);


        // 初始化数据模型变量
        gInfo = gInfoDao.query();
        uid = gInfo.getActiveUserUid();
        uInfo = uInfoDao.query(uid);
    }
}

