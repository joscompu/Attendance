package com.osm.attendance.util;

import java.util.Calendar;
import java.util.Date;

public class Time {

    public static int getCurrentDayNUmber(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK) -2;
    }
}
