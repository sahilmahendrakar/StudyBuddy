package com.studybuddy.sahilmahendrakar.studybuddy.utilities;

import android.util.Log;

import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Scheduler {
    private final static String TAG = Scheduler.class.getSimpleName();
    private List<Event> mEvents;
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;
    private ArrayList<ArrayList<Long>> freeTime;

    public Scheduler(List<Event> events, Calendar startCalendar, Calendar endCalendar) {
        mEvents = events;
        mStartCalendar = startCalendar;
        mEndCalendar = endCalendar;
        freeTime = new ArrayList<>();
    }
    //Given an event and the length of that event in milliseconds
    //determines when the best time for the event to be in is
    public Event scheduleEvent(Event event, long timeLength){
        Log.d(TAG, mEvents.toString()); //for debugging
        setUpFreeTimeArray(); //finds when the user is free
        Log.d(TAG, Arrays.toString(freeTime.toArray()));
        //todo: sort freeTime
        //todo: error catching
        //finds the max free time interval the user has from now until the due date of the assignment
        ArrayList<Long> freeTimeInterval = findMaxInterval();
        //finds the length of the max interval
        long freeTimeLength = freeTimeInterval.get(1) - freeTimeInterval.get(0);
        //if the event can't fit in the interval, puts default times
        if (freeTimeLength < timeLength){
            //sets times of event
            event.setStartTime(Calendar.getInstance());
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(c.getTimeInMillis() + timeLength);
            event.setEndTime(c);
            return null;
        }
        //adds a bit of a buffer between adjacent events
        long buffer = ((freeTimeInterval.get(1) - freeTimeInterval.get(0)) - timeLength)/2;
        //sets start and end times
        event.setStartTime(freeTimeInterval.get(0) + buffer);
        event.setEndTime(freeTimeInterval.get(0) + buffer + timeLength);
        //returns event
        return event;
    }

    //Finds the longest free time interval in the freeTime array
    private ArrayList<Long> findMaxInterval(){
        ArrayList<Long> max = freeTime.get(0);
        //iterates through arraylist and finds max length
        for(ArrayList<Long> interval : freeTime){
            if((interval.get(1) - interval.get(0)) > (max.get(1) - max.get(0))){
                max = interval;
            }
        }
        return max;
    }

    //Algorithm that uses mEvents to find when the user is busy. It then uses these times
    //to determine when the user is free and puts those intervals in the freeTime arraylist
    private void setUpFreeTimeArray(){
        //imagine the day as a line, this algorithm chops up the line and removes times when the user
        //is busy
        //initializes freeTimeInterval which represents time from current time until event is due
        ArrayList<Long> freeTimeInterval =
                new ArrayList<Long>(Arrays.asList(mStartCalendar.getTimeInMillis(), mEndCalendar.getTimeInMillis()));
        //adds interval to freeTime array
        freeTime.add(freeTimeInterval);
        //creates a copy of the array to avoid a ConcurrentModificationException
        ArrayList<ArrayList<Long>> freeTimeCopy = (ArrayList<ArrayList<Long>>) freeTime.clone();
        //iterates through each events
        for(Event event : mEvents) {
            //gets start and end time of event
            long start = event.getStartTime().getTimeInMillis();
            long end = event.getEndTime().getTimeInMillis();
            //iterates through each interval in free time
            for (ArrayList<Long> interval : freeTime) {
                //removes the times when the event is already there by splitting the interval
                if (interval.get(0) < start && start < interval.get(1)) {
                    ArrayList<Long> firstInterval = new ArrayList<Long>();
                    firstInterval.add(interval.get(0));
                    firstInterval.add(start); //splits interval
                    freeTimeCopy.add(firstInterval);
                    freeTimeCopy.remove(interval);
                }
                //removes the times when the event is already there by splitting the interval
                if (end < interval.get(1) && end > interval.get(0)) {
                    ArrayList<Long> firstInterval = new ArrayList<Long>();
                    firstInterval.add(end);
                    firstInterval.add(interval.get(1));
                    freeTimeCopy.add(firstInterval);
                    freeTimeCopy.remove(interval);
                }
                //removes the times when the event is already there by splitting the interval
                if (interval.get(0) > start && interval.get(1) < end) {
                    freeTimeCopy.remove(interval);
                }
            }
            //freeTime = freeTimeCopy
            freeTime = freeTimeCopy;
            //resets freeTimeCopy
            freeTimeCopy = (ArrayList<ArrayList<Long>>) freeTime.clone();
        }
    }
    ///getters and setters///
    public void setEvents(List<Event> mEvents) {
        this.mEvents = mEvents;
    }

    public void setStartCalendar(Calendar mStartCalendar) {
        this.mStartCalendar = mStartCalendar;
    }

    public void setEndCalendar(Calendar mEndCalendar) {
        this.mEndCalendar = mEndCalendar;
    }
}
