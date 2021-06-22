package com.example.car.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return formatter.format(curDate);
    }

    public static String getNextTime(String currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date curDate = formatter.parse(currentTime);
            Calendar calendar = Calendar.getInstance();
            //当前用户输入的时间传入Calendar类
            calendar.setTime(curDate);
            //当前用户输入的时间加上180天
            calendar.add(Calendar.DATE, 180);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

