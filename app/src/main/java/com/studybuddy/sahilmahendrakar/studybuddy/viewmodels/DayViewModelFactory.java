package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import com.studybuddy.sahilmahendrakar.studybuddy.utilities.EventHelper;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

//ViewModelFactory for DayViewModel. Passes it parameters of event helper and times
public class DayViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final EventHelper mEventHelper; //helps with database transactions
    //timestamps to be passed to view model
    private final long startTime;
    private final long endTime;

    //constructor
    public DayViewModelFactory(EventHelper mEventHelper, long startTime, long endTime) {
        this.mEventHelper = mEventHelper;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //creates view model with parameters
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new DayViewModel(mEventHelper, startTime, endTime);
    }
}
