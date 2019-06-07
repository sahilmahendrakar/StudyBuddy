package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

//ViewModelFactory for EventViewModel with parameters database and event it
public class EventViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final EventDatabase mDatabase; //database to be passed to view model
    private final int mEventId; //event id to be passed to view model

    //constructor
    public EventViewModelFactory(EventDatabase mDatabase, int mEventId) {
        this.mDatabase = mDatabase;
        this.mEventId = mEventId;
    }

    //creates view model with parameters
    public <T extends ViewModel> T create(Class<T> modelClass){
        if(mEventId == -1){ //if default event id is passed
            return (T) new EventViewModel(mDatabase);
        }
        return (T) new EventViewModel(mDatabase, mEventId);
    }
}
