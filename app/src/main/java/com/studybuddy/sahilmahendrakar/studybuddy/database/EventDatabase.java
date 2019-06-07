package com.studybuddy.sahilmahendrakar.studybuddy.database;

import android.content.Context;
import android.util.Log;

import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//Database that holds events and categories
@Database(entities = {Event.class, Category.class}, version = 4)
@TypeConverters(CalendarConverter.class)
public abstract class EventDatabase extends RoomDatabase {
    //tag for debugging
    private static final String LOG_TAG = EventDatabase.class.getSimpleName();
    //used for synchronization
    private static final Object LOCK = new Object();
    //database name
    private static final String DATABASE_NAME = "studdybuddy";
    //instance that will be passed to accessors
    private static EventDatabase sInstance;

    //gets instance of database
    public static EventDatabase getInstance(Context context) {
        if (sInstance == null) { //if the instance has not been initialized yet
            synchronized (LOCK) { //synchronized method so multiple instances aren't created at once
                Log.d(LOG_TAG, "Creating new database instance");
                //creates the instance
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        EventDatabase.class, EventDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance; //returns instance
    }
    //returns eventdao
    public abstract EventDao eventDao();
    //returns categorydao
    public abstract CategoryDao categoryDao();
}
