package com.studybuddy.sahilmahendrakar.studybuddy.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.adapters.CategorySpinnerAdapter;
import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.EventRepetition;
import com.studybuddy.sahilmahendrakar.studybuddy.executors.AppExecutors;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.EventViewModel;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.EventViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//activity for adding or editing an event
//layout file: activity_event.xml
public class EventActivity extends AppCompatActivity {
    //Tag for debugging
    private static final String TAG = EventActivity.class.getSimpleName();

    //for putting and extracting data from bundles
    public static final String EXTRA_EVENT_ID = "extraEventId";
    public static final String INSTANCE_EVENT_ID = "instanceTaskId";

    //mEventId id of event that is being updated
    private static final int DEFAULT_EVENT_ID = -1;
    private int mEventId = DEFAULT_EVENT_ID;

    //UI views that the user can interact with and enter information
    private EditText mTitle;
    private EditText mDescription;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;

    //calendars used to hold the start and end times that the user enters
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;

    //Formatters to format date and time into readable strings
    private DateFormat mDateFormatter;
    private SimpleDateFormat mTimeFormatter;

    //spinner for the user to pick a category from a list of categories
    private ArrayList<Category> mCategories;
    private Spinner mCategorySpinner;

    //database for getting events and categories
    private EventDatabase mDatabase;

    //event if one is being updated
    private Event mEvent;

    //checkbox for repetition
    private CheckBox mRepeatDailyCheckBox;

    //called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event); //inflates layout

        //Attaches views
        initViews();

        //Sets formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        mTimeFormatter = new SimpleDateFormat("h:mm a");

        //sets default values for start and end times
        mStartCalendar = Calendar.getInstance();
        mEndCalendar = Calendar.getInstance();
        mEndCalendar.add(Calendar.HOUR, 1);
        setDateTimeViews(); //updates UI with times

        //gets instance for database
        mDatabase = EventDatabase.getInstance(this);

        //sets on click listeners
        setOnClickListeners();

        //instantiates mCategories
        if(mCategories == null) mCategories = new ArrayList<>();

        //gets eventID from the bundle if there is one
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_EVENT_ID)) {
            mEventId = savedInstanceState.getInt(INSTANCE_EVENT_ID, DEFAULT_EVENT_ID);
        }
        //gets intent that started the actviity
        Intent intent = getIntent();
        //checks if the intent has an event id
        if(intent != null && intent.hasExtra(EXTRA_EVENT_ID)){
            //if it does, it uses a view model to get the event from a datagbase
            if(mEventId == DEFAULT_EVENT_ID){
                mEventId = intent.getIntExtra(EXTRA_EVENT_ID, DEFAULT_EVENT_ID);
                //creates a factory with mDatabase and event id
                EventViewModelFactory factory = new EventViewModelFactory(mDatabase, mEventId);
                //creates view model from factory
                final EventViewModel eventViewModel =
                        ViewModelProviders.of(this, factory).get(EventViewModel.class);
                //attaches listener to update UI when it gets an event
                eventViewModel.getEvent().observe(this, new Observer<Event>() {
                    @Override
                    public void onChanged(Event event) {
                        mEvent = event;
                        eventViewModel.getEvent().removeObserver(this);
                        populateUI(event);
                    }
                });
            }
        }
        //creates factory with database and event id
        EventViewModelFactory factory = new EventViewModelFactory(mDatabase, mEventId);
        //creates a new eventViewModel
        EventViewModel eventViewModel =
                ViewModelProviders.of(this, factory).get(EventViewModel.class);
        //attaches listener to categories from database
        eventViewModel.getCategoryList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                mCategories = new ArrayList<>(categories);
                setUpSpinner(); //updates spinner if categories are changed
            }
        });
        //updates ui if Activity is updating an existing event
        if(mEventId != DEFAULT_EVENT_ID) populateUI(mEvent);
    }

    //called when the activity is closed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //saves event id in a bundle
        outState.putInt(INSTANCE_EVENT_ID, mEventId);
        super.onSaveInstanceState(outState);
    }
    //attaches views to corresponding layout components
    private void initViews(){
        //attaches views
        mTitle = (EditText) findViewById(R.id.event_title);
        mDescription = (EditText) findViewById(R.id.scheduled_event_description);
        mCategorySpinner = (Spinner) findViewById(R.id.scheduled_spinner_categories);
        mStartDate = (TextView) findViewById(R.id.start_date);
        mEndDate = (TextView) findViewById(R.id.end_date);
        mStartTime = (TextView) findViewById(R.id.start_time);
        mEndTime = (TextView) findViewById(R.id.end_time);
        mRepeatDailyCheckBox = (CheckBox) findViewById(R.id.cb_event_repeat_daily);
    }

    //updates ui if the Activity is updating an event
    private void populateUI(Event event){
        //does nothing if the event is null
        if(event == null){
            return;
        }
        //sets UI Views
        mTitle.setText(event.getTitle());
        mDescription.setText(event.getDescription());
        mStartCalendar = event.getStartTime();
        mEndCalendar = event.getEndTime();
        //updates category spinner (bug)
        for(int i = 0; i < mCategories.size(); i++){
            if(event.getCategory().equals(mCategories.get(i))) //todo fix this
                mCategorySpinner.setSelection(i);
        }
        setDateTimeViews(); //updates date and time views
    }


    //sets up spinner to display Categories
    private void setUpSpinner(){
        //Uses Category SpinnerAdapter
        CategorySpinnerAdapter mCategorySpinnerAdapter = new CategorySpinnerAdapter(this, mCategories);
        mCategorySpinner.setAdapter(mCategorySpinnerAdapter);
        //callback for when an item is selected
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedItem = (Category) parent.getItemAtPosition(position);
                String categoryName = selectedItem.getTitle(); //gets position and name
            }
            //callback if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //sets the on click listeners for date/time views
    private void setOnClickListeners(){
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(mStartCalendar);
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(mEndCalendar);
            }
        });
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(mStartCalendar);
            }
        });
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(mEndCalendar);
            }
        });
    }
    //opens a date picker dialogue and saves it in initDate Calendar
    private void pickDate(final Calendar initDate){
        //callback for when date is selected
        DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                initDate.set(year, month, dayOfMonth, //saves in calendar
                        initDate.get(Calendar.HOUR_OF_DAY),
                        initDate.get(Calendar.MINUTE),
                        initDate.get(Calendar.SECOND));
                setDateTimeViews(); //updates views
            }
        };
        //creates dialogue
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                setDate,
                initDate.get(Calendar.YEAR),
                initDate.get(Calendar.MONTH),
                initDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(); //shows dialogue
    }
    //opens a time picker dialogue and saves result in initTime Calendar
    private void pickTime(final Calendar initTime){
        //callback for when time is selected
        TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                initTime.set( //saves in calendar
                        initTime.get(Calendar.YEAR),
                        initTime.get(Calendar.MONTH),
                        initTime.get(Calendar.DATE),
                        hourOfDay,
                        minute
                );
                setDateTimeViews(); //updates views
            }
        };
        //creates dialogue
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                setTime, //callback
                initTime.get(Calendar.HOUR_OF_DAY),
                initTime.get(Calendar.MINUTE),
                false);
        timePickerDialog.show(); //shows dialogue
    }

    //updates date and time views
    private void setDateTimeViews(){
        setStartDate();
        setStartTime();
        setEndDate();
        setEndTime();
    }
    //formats date and sets it to mStartDate TextView
    private void setStartDate(){
        String formattedDate = mDateFormatter.format(mStartCalendar.getTime());
        mStartDate.setText(formattedDate);
    }
    //formats time and sets it
    private void setStartTime(){
        mStartTime.setText(mTimeFormatter.format(mStartCalendar.getTime()));
    }
    //formats time and sets it
    private void setEndTime(){
        mEndTime.setText(mTimeFormatter.format(mEndCalendar.getTime()));
    }
    //formats date and sets it
    private void setEndDate(){
        String formattedDate = mDateFormatter.format(mEndCalendar.getTime());
        mEndDate.setText(formattedDate);
    }
    //called when the save button is clicked
    public void onSaveButtonClicked(MenuItem item){
        //gets title form mTitle
        String title = mTitle.getText().toString();
        //gets description from mDescription
        String description = mDescription.getText().toString();
        //gets category from spinner
        Category category = mCategories.get(mCategorySpinner.getSelectedItemPosition());
        EventRepetition eventRepetition = null;
        //sets up EventRepetition object
        if(mRepeatDailyCheckBox.isChecked()){
            int hour = mStartCalendar.get(Calendar.HOUR_OF_DAY);
            long minute = mStartCalendar.get(Calendar.MINUTE);
            long length = mEndCalendar.getTimeInMillis() - mStartCalendar.getTimeInMillis();
            eventRepetition = new EventRepetition(length, minute, hour, EventRepetition.REPEAT_DAILY);
        }
        if(mStartCalendar.getTimeInMillis() > mEndCalendar.getTimeInMillis()){
            Toast.makeText(this, "The start time must be before the end time", Toast.LENGTH_LONG).show();
            return;
        }
        final Event event = new Event(title, description, mStartCalendar, mEndCalendar, category, eventRepetition);
        //inserts/updates event into database on a separate thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mEventId == DEFAULT_EVENT_ID) {
                    // insert new event
                    mDatabase.eventDao().insertEvent(event); //inserts event
                } else {
                    //update event
                    event.setId(mEventId);
                    mDatabase.eventDao().updateEvent(event);
                }
                finish(); //closes activity
            }

        });
    }
    //called when the add category button is clicked
    public void onAddCategoryButtonClicked(View v){
        //starts addcategory activity
        Intent addCategoryIntent = new Intent(this, AddCategoryActivity.class);
        startActivity(addCategoryIntent);
    }

    //inflates menue
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }
    //called when the delete button is clicked
    public void onDeleteButtonClicked(MenuItem item){
        if(mEventId == DEFAULT_EVENT_ID){ //if new event
            finish(); //closes activity
        }
        else { //if event is being updated
            //deletes event on a separate thread
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.eventDao().deleteEvent(mEvent);
                    finish();
                }
            });
        }
    }
}
