package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
//viewmodel for AddAssignmentActivity. Gets events from database
public class AddScheduledEventViewModel extends ViewModel {
    private LiveData<List<Event>> events; //events from database

    //constructor gets events from database
    public AddScheduledEventViewModel(EventDatabase database, long fromTime){
        events = database.eventDao().getEventsFrom(fromTime);
    }
    //returns events from database
    public LiveData<List<Event>> getEvents() {
        return events;
    }

}
