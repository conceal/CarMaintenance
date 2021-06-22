package com.example.car.util;

import java.util.ArrayList;
import java.util.List;

public class AppointmentTimeUtil {
    public static List<String> calculateTime(String business_time) {
        List<String> list = new ArrayList<>();
        int startTime = Integer.parseInt(business_time.substring(0, 2));
        int endTime = Integer.parseInt(business_time.substring(6, 8));
        while (startTime < endTime) {
            String time1 = String.valueOf(startTime) + ":00-" + String.valueOf(startTime) + ":30";
            list.add(time1);
            int nextTime = startTime + 1;
            String time2 = String.valueOf(startTime) + ":30-" + String.valueOf(nextTime) + ":00";
            list.add(time2);
            startTime++;
        }
        return list;
    }
}
