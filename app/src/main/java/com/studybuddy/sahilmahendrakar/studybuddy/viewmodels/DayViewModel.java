package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import android.app.Application;
import android.util.Log;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.EventHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

//ViewModel for DayFragment. Gets events with a time interval
public class DayViewModel extends ViewModel {
    private EventHelper mEventHelper; //helps with database transactinos
    private long startTime; //when to start getting events from
    private long endTime; //when to stop getting events from
    private LiveData<List<Event>> events; //events from database

    //constructor, gets events from database
    public DayViewModel(EventHelper eventHelper, long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        mEventHelper = eventHelper;
        events = loadEvents();
    }

    //gets events from database
    private LiveData<List<Event>> loadEvents(){
        return mEventHelper.getEventsFromTo(startTime, endTime);
    }

    //gets events from database
    public LiveData<List<Event>> getEvents() {
        if(events == null){
            events = loadEvents();
        }
        return events;
    }
}
