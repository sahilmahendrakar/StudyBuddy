package com.studybuddy.sahilmahendrakar.studybuddy.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.activities.EventActivity;
import com.studybuddy.sahilmahendrakar.studybuddy.activities.MainActivity;
import com.studybuddy.sahilmahendrakar.studybuddy.database.CalendarConverter;
import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.executors.AppExecutors;
import com.studybuddy.sahilmahendrakar.studybuddy.gestures.OnSwipeTouchListener;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.EventHelper;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.DayViewModel;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.DayViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//Fragment for the main view of the app. Shows events of the day
public class DayFragment extends Fragment {
    //tag for debugging
    private static String TAG = DayFragment.class.getSimpleName();
    //id for putting or retrieving times from a bundle
    private static String START_DAY = "start_day";

    //calendars that hold times for the start of the day and the end of the day
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;

    //Views
    private TextView mHeader; //date header
    private RelativeLayout mRelativeLayout; //underlying layout of fragment

    //event helper to help with database transactions
    private EventHelper mEventHelper;
    private EventDatabase mDatabase; //database to retrieve events and categories

    public DayFragment() {
        // Required empty public constructor
    }

    //creates a new fragment with a given start time
    public static DayFragment newInstance(long startTime){
        Bundle bundle = new Bundle();
        //puts the start time in a bundle with id START_DAY
        bundle.putLong(START_DAY, startTime);
        //creates a new fragment
        DayFragment fragment = new DayFragment();
        fragment.setArguments(bundle); //attaches bundle
        //returns the fragment
        return fragment;
    }
    //reads from time from bundle
    private void readBundle(Bundle bundle){
        if(bundle != null){
            //sets start calendar and end calendar based on time in bundle
            mStartCalendar = Calendar.getInstance();
            mStartCalendar.setTimeInMillis(bundle.getLong(START_DAY)); //gets time from bundle
            mEndCalendar = (Calendar) mStartCalendar.clone();
            mEndCalendar.add(Calendar.DATE, 1); //end calendar is one day after start calendar
        }
    }

    //called when the fragment is first created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        readBundle(getArguments()); //reads bundle
        //instantiates event helper
        mEventHelper = new EventHelper(getContext());
        //assigns views to corresponding layout component
        mRelativeLayout = view.findViewById(R.id.event_item_relative_layout);
        mHeader = view.findViewById(R.id.tv_day_header);
        //instantiates database
        mDatabase = EventDatabase.getInstance(getContext());
        //sets up view model
        setupViewModel();
        //sets up header
        setUpHeader();
        //returns the view
        return view;
    }
    //sets up view model
    private void setupViewModel(){
        Log.d(TAG, "setting up view model");
        //Creates a Day View Model Factory with the event helper, the start time, and the end time
        DayViewModelFactory factory = new DayViewModelFactory(mEventHelper, mStartCalendar.getTimeInMillis(), mEndCalendar.getTimeInMillis());
        //gets viewmodel from factory
        DayViewModel dayViewModel = ViewModelProviders.of(this, factory).get(DayViewModel.class);
        //attaches listener that updates the calendar when events change
        dayViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                updateCalendar(events);
            }
        });
    }
    //probably the longest method in this entire application
    //for each event in the day, it creates a text view and attaches it to the relative layout
    private void updateCalendar(List<Event> eventList){
        for(final Event event : eventList){
            Log.d(TAG, event.toString()); //for debugging
            //creates text view
            final TextView textView = new TextView(getContext());
            //sets title of text
            textView.setText(event.getTitle());
            //Sets background and color
            textView.setBackground(getResources().getDrawable(R.drawable.event_background));
            GradientDrawable background = (GradientDrawable) textView.getBackground();
            background.setColor(event.getCategory().getColor());
            //sets height of the text view depending on how long the event is
            int height = getLength(event.getStartTime().getTimeInMillis(), event.getEndTime().getTimeInMillis());
            int width = mRelativeLayout.getWidth() - 300;
            textView.setHeight(height);
            textView.setWidth(width);
            //textView.setBackgroundColor(event.getCategory().getColor());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            //sets parameters for size
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int topMargin = getLength(mStartCalendar.getTimeInMillis(), event.getStartTime().getTimeInMillis());
            layoutParams.setMargins(0, topMargin,64, 0);
            //ensures textView isn't too long and stretches screen
            if((topMargin + height) > getResources().getDimensionPixelSize(R.dimen.DayViewHeight)){
                textView.setHeight(getResources().getDimensionPixelSize(R.dimen.DayViewHeight) - topMargin);
            }
            //aligns the textviews to the right
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //sets parameters for layout
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER); //centers the text in the text view
            textView.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Headline);
            //sets textview size
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            //sets text color
            textView.setTextColor(Color.WHITE);
            //ADDS THE VIEW TO THE LAYOUT!
            mRelativeLayout.addView(textView);
            //gets event id
            final int eventId = event.getId();
            //sets on touch listener to respond to gestures
            textView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
                @Override
                public void onClick() {
                    //on click, opens the event activity with the event
                    super.onClick();
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra(EventActivity.EXTRA_EVENT_ID, eventId);
                    startActivity(intent);
                }


                @Override
                public void onLongClick() {
                    //on long click, deletes the event
                    super.onLongClick();
                    textView.setVisibility(View.GONE);
                    //deletes event on a separate thread
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.eventDao().deleteEvent(event);

                        }
                    });
                }
                //if swiped, deletes event
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.eventDao().deleteEvent(event);
                            textView.setVisibility(View.GONE);
                        }
                    });
                }
                //if swiped, deletes event
                @Override
                public void onSwipeRight() {
                    super.onSwipeLeft();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.eventDao().deleteEvent(event);
                            textView.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    //converts time length to pixel length
    private int getLength(long startTime, long endTime){
        //simple algorithm just converts milliseconds to pixels in day basically
        long millisInDay = mEndCalendar.getTimeInMillis() - mStartCalendar.getTimeInMillis();
        float inPixels = ((float) (endTime - startTime)/millisInDay)*(getResources().getDimensionPixelSize(R.dimen.DayViewHeight));
        return (int) (inPixels);
    }

    //called right after the fragment is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //for each hour in a day
        for(int i = 0; i < 24; i++){
            //creates a text view
            TextView textView = new TextView(getContext());
            //converts int i into a time string
            textView.setText(intToTime(i));
            //creates layout parameters
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            //determines how far the time should be from the top
            float topMargin = ((float) (i)/24)*(getResources().getDimensionPixelSize(R.dimen.DayViewHeight));
            //sets margins
            layoutParams.setMargins(16, (int) topMargin,0, 0);
            textView.setLayoutParams(layoutParams);
            //adds text view
            mRelativeLayout.addView(textView);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    //converts an int (0-24) into a time String
    private String intToTime(int i){
        StringBuilder stringBuilder = new StringBuilder();
        if(i == 0){
            return "12:00 AM";
        }
        if(i < 12){
            return i+ ":00 AM";
        }
        if(i == 12){
            return i+":00 PM";
        }
        //i > 12
        else return (i-12) + ":00 PM";
    }
    //sets up header by formatting date and setting it to the view
    private void setUpHeader(){
        DateFormat format = SimpleDateFormat.getDateInstance();
                //new SimpleDateFormat("EEE, MMMMM dd");
        mHeader.setText(format.format(mStartCalendar.getTime()));
    }

}
