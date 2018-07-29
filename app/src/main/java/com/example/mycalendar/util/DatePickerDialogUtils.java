package com.example.mycalendar.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by 逍遥依尘 on 2018/5/26.
 */

public class DatePickerDialogUtils {
    private DatePickerDialog datePickerDialog;
    private Button datePickerBtn;
    private Calendar calendar;
    private int year,month,day;
    private Context mContext;
    private String date;

    public DatePickerDialogUtils(Context context){
        mContext = context;
        calendar =Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //date = year + "-" + String.format("%02d",month + 1) + "-" + String.format("%02d",day) + "-";
    }

    public void initDatePickerDialog(final Button button){
        datePickerDialog = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "-" + String.format("%02d",i1 + 1) + "-" + String.format("%02d",i2);
                button.setText(date);
            }
        },year,month,day);

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }

    public String getDate(){
        return date;
    }

}
