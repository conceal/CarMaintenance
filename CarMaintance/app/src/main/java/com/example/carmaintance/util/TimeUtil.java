package com.example.carmaintance.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return formatter.format(curDate);
    }
}
