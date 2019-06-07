package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

//ViewModel for EventActivity. Gets events and categories from database
public class EventViewModel extends ViewModel {
    private LiveData<Event> event; //event from database
    private LiveData<List<Category>> categoryList; //categories from database

    //constructor, gets event and categories from database
    public EventViewModel(EventDatabase database, int eventId){
        event = database.eventDao().getEventWithId(eventId);
        categoryList = database.categoryDao().getAllCategories();
    }

    //constructor if no eventid is given
    public EventViewModel(EventDatabase database){
        categoryList = database.categoryDao().getAllCategories();
    }
    //returns event
    public LiveData<Event> getEvent() {
        return event;
    }
    //returns categories
    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }
}
