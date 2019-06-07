package com.studybuddy.sahilmahendrakar.studybuddy.database;

import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

//Interface for transactions with event table
@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY start_time")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM event WHERE repetition_repetition_rule = :repetitionRule")
    LiveData<List<Event>> getEventsWithRepetitionRule(int repetitionRule);

    @Query("SELECT * FROM event WHERE category_id = :categoryId")
    LiveData<List<Event>> getEventsWithCategoryId(int categoryId);

    @Query("SELECT * FROM event WHERE start_time <= :toTime AND end_time >= :fromTime")
    LiveData<List<Event>> getEventsFromToTime(long fromTime, long toTime);

    @Query("SELECT * FROM event WHERE start_time <= :toTime AND end_time >= :fromTime")
    List<Event> getEventsFromToTimeList(long fromTime, long toTime);

    @Query("SELECT * FROM event WHERE end_time >= :fromTime")
    LiveData<List<Event>> getEventsFrom(long fromTime);

    @Query("SELECT * FROM event WHERE id = :id")
    LiveData<Event> getEventWithId(int id);

    @Insert
    void insertEvent(Event event);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

}
