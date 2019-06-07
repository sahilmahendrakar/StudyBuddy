package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

//ViewModelFactory for AddScheduledEventViewModel
//gives it a database and start time
public class AddScheduledEventViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final EventDatabase mDatabase; //database to be passed to view model
    private final long mFromTime; //timestamp of where to start getting events from

    //constructor
    public AddScheduledEventViewModelFactory(EventDatabase mDatabase, long fromTime) {
        this.mDatabase = mDatabase;
        this.mFromTime = fromTime;
    }

    //creates view model with parameters
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new AddScheduledEventViewModel(mDatabase, mFromTime);
    }
}