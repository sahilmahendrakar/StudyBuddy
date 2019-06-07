package com.studybuddy.sahilmahendrakar.studybuddy.database;


import java.util.Calendar;

import androidx.room.TypeConverter;
//TypeConverter for EventDatabase
//converts to and from Calendar and long
public class CalendarConverter{
    @TypeConverter
    public static Calendar toCalendar(Long timestamp){
        if(timestamp == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        return c;
    }

    @TypeConverter
    public static Long toTimestamp(Calendar calendar){
        return calendar == null ? null : calendar.getTime().getTime();
    }
}
