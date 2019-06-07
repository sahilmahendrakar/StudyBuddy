package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import android.app.Application;
import android.util.Log;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//ViewModel for MainActivity. Gets events and categories from database
public class MainViewModel extends AndroidViewModel {
    //tag for debugging
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Event>> events; //events from database
    private LiveData<List<Category>> categories; //categories from database
    //constructur, gets events and categories from database
    public MainViewModel(@NonNull Application application) {
        super(application);
        EventDatabase database = EventDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively Retrieving the events from database");
        events = database.eventDao().getAllEvents();
        categories = database.categoryDao().getAllCategories();
    }
    //returns events
    public LiveData<List<Event>> getEvents() {
        return events;
    }
    //returns categories
    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
