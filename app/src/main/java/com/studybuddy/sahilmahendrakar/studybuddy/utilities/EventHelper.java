package com.studybuddy.sahilmahendrakar.studybuddy.utilities;

import android.content.Context;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.executors.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

//Class that helps with database transactions
public class EventHelper {
    //instance of event database
    private EventDatabase mDb;
    //constructor
    public EventHelper(Context context){
        mDb = EventDatabase.getInstance(context);
    }
    //returns a live data list of events from database
    public LiveData<List<Event>> getEventsFromTo(long startTime, long endTime){
        return mDb.eventDao().getEventsFromToTime(startTime, endTime);
        //TODO: implement repetition
        //use hour and minute to create the start time for the event, then use length to find the end time
    }
}
