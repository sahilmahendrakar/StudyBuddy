package com.studybuddy.sahilmahendrakar.studybuddy.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
//Really Important class!
//Event object that represents events the user enter
//stored in database table events
@Entity(tableName = "event")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id; //unique id
    private String title; //title of event
    private String description; //description of event
    @ColumnInfo(name = "start_time")
    private Calendar startTime; //start time of event
    @ColumnInfo(name = "end_time")
    private Calendar endTime; //end time of event
    @Embedded(prefix = "category_")
    private Category category; //holds a category object
    @Embedded(prefix = "repetition_")
    private EventRepetition eventRepetition; //holds eventrepetition object

    //constructor
    @Ignore
    public Event(String title, String description, Calendar startTime, Calendar endTime, Category category) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
    //overloaded constructor
    @Ignore
    public Event(String title, String description, Calendar startTime, Calendar endTime, Category category, EventRepetition eventRepetition) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.eventRepetition = eventRepetition;
    }
    //constructor used by database
    public Event(int id, String title, String description, Calendar startTime, Calendar endTime, Category category, EventRepetition eventRepetition) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.eventRepetition = eventRepetition;
    }
    ///getters and setters///
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public EventRepetition getEventRepetition() {
        return eventRepetition;
    }

    public void setEventRepetition(EventRepetition eventRepetition) {
        this.eventRepetition = eventRepetition;
    }

    //overloaded for long time in millis
    public void setStartTime(long startTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startTime);
        this.startTime = c;
    }
    //overloaded for long time in millis
    public void setEndTime(long endTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(endTime);
        this.endTime = c;
    }

    //simple to string method
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", category=" + category +
                '}';
    }
}