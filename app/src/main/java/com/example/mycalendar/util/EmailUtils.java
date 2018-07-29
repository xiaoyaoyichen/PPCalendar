package com.example.mycalendar.util;

import android.text.TextUtils;

/**
 * Created by 逍遥依尘 on 2018/5/16.
 */

public class EmailUtils {
    public static boolean isEmail(String strEmail) {
        String strPattern = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }
}
