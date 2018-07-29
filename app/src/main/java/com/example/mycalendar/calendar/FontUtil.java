package com.example.mycalendar.calendar;

import android.graphics.Paint;

/**
 * Created by 逍遥依尘 on 2018/7/1.
 */

public class FontUtil {

    /**
     * @param paint
     * @param str
     * @return 返回指定笔和指定字符串的长度
     * @add yujiangtao 16/8/5
     */
    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @return 返回指定笔的文字高度
     * @add yujiangtao 16/8/5
     */
    public static float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
    /**
     * @return 返回指定笔离文字顶部的基准距离
     * @add yujiangtao 16/8/5
     */
    public static float getFontLeading(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading- fm.ascent;
    }


}