package com.studybuddy.sahilmahendrakar.studybuddy.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

//Object that determines how an event will repeat
@Entity
public class EventRepetition {
    //static ints for repetition rules
    public static final int REPEAT_DAILY = 1;
    public static final int REPEAT_WEEKLY = 2;
    public static final int REPEAT_MONTHLY = 3;

    private long length; //length of event
    private long minute; //start minute of event
    private int hour; //start hour of event
    private int day; //what day to repeat on
    private int week; //weak to repeat on
    private int month; //month to repeat on
    @ColumnInfo(name = "repetition_rule")
    private int repetitionRule; //rule of how to repeat, ex. repeat daily = 1

    //constructor
    @Ignore
    public EventRepetition(long length, long minute, int hour, int repetitionRule) {
        this.length = length;
        this.minute = minute;
        this.hour = hour;
        this.repetitionRule = repetitionRule;
    }

    //constructor
    public EventRepetition(long length, long minute, int hour, int day, int week, int month, int repetitionRule) {
        this.length = length;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.week = week;
        this.month = month;
        this.repetitionRule = repetitionRule;
    }

    ///getters and setters///
    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getRepetitionRule() {
        return repetitionRule;
    }

    public void setRepetitionRule(int repetitionRule) {
        this.repetitionRule = repetitionRule;
    }
}
