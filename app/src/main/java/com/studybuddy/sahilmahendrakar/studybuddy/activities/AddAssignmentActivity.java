package com.studybuddy.sahilmahendrakar.studybuddy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.studybuddy.sahilmahendrakar.studybuddy.executors.AppExecutors;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.Scheduler;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.AddScheduledEventViewModel;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.AddScheduledEventViewModelFactory;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.EventViewModel;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.EventViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Activity that provides a user interface to add an assignment that will be scheduled by the app
//Layout file: activity_add_assignment.xml
public class AddAssignmentActivity extends AppCompatActivity {

    //Id of an event item that can be passed to EventActivity
    private static final String EXTRA_EVENT_ID = "extraEventId";
    //If the event is closed unexpectedly, it saves the event id in a bundle with this id.
    private static final String INSTANCE_EVENT_ID = "instanceTaskId";
    //If the event is closed unexpectedly, it saves the entered time in a bundle with this id.
    private static final String INSTANCE_TIME = "instanceTime";

    private static final int DEFAULT_EVENT_ID = -1;
    private int mEventId = DEFAULT_EVENT_ID;

    //Views for the user to enter information
    private EditText mTitle;
    private EditText mDescription;
    private EditText mHour;
    private EditText mMinute;
    //Views for the user to enter date and time
    private TextView mDueDate;
    private TextView mDueTime;

    //Calendar object that represents the due date the user entered
    private Calendar mDueDateCalendar;

    //date and time formatters to format readable strings
    private DateFormat mDateFormatter;
    private SimpleDateFormat mTimeFormatter;

    //spinner to display a list of categories
    private Spinner mCategorySpinner;
    private ArrayList<Category> mCategories;

    //event database to get categories and events from
    private EventDatabase mDatabase;
    private Event mEvent;

    //length of the time in milliseconds. Formed from mHour and mMinute
    private long timeLengthInMillis;

    //Scheduler object to schedule the event for a certain time
    private Scheduler mScheduler;



    //called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment); //inflates layout
        initViews(); //initializes views

        //gets instance for database
        mDatabase = EventDatabase.getInstance(this);

        //sets date and time formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        mTimeFormatter = new SimpleDateFormat("h:mm a");

        //gets time from INSTANCE_TIME if the activity is being recreated after being suddenly closed
        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TIME)){
            long selectedTime = savedInstanceState.getLong(INSTANCE_TIME);
            Calendar temp = Calendar.getInstance();
            temp.setTimeInMillis(selectedTime);
            mDueDateCalendar = temp;
        } else mDueDateCalendar = Calendar.getInstance(); //if not, the default due date is the current time

        setDateTimeViews(); //sets mDueDate and mDueTime depending on the mDueDateCalendar

        //gets mEventId from bundle
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_EVENT_ID)) {
            mEventId = savedInstanceState.getInt(INSTANCE_EVENT_ID, DEFAULT_EVENT_ID);
        }

        //gets the intent used to start the Activity
        Intent intent = getIntent();
        //checks if an event id was given to the class
        if(intent != null && intent.hasExtra(EXTRA_EVENT_ID)){
            if(mEventId == DEFAULT_EVENT_ID){
                mEventId = intent.getIntExtra(EXTRA_EVENT_ID, DEFAULT_EVENT_ID);
                //uses viewmodel factory to create a new event view model with database and id
                EventViewModelFactory factory = new EventViewModelFactory(mDatabase, mEventId);
                final EventViewModel eventViewModel =
                        ViewModelProviders.of(this, factory).get(EventViewModel.class);
                //attaches listener to event
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
        //sets up view models
        setUpViewModels();
        //sets on click listeners to mDueDate and mDueTime TextViews
        setOnClickListeners();
    }

    private void setUpViewModels(){
        //creates a new Factory with database and id
        EventViewModelFactory factory = new EventViewModelFactory(mDatabase, mEventId);
        EventViewModel eventViewModel =
                ViewModelProviders.of(this, factory).get(EventViewModel.class);
        //Uses view model to get category list
        //listener is called any time the categories are changed or updated
        eventViewModel.getCategoryList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                mCategories = new ArrayList<>(categories);
                setUpSpinner(); //sets up spinner if categories change
            }
        });

        //creates a new Factory with database and current time
        AddScheduledEventViewModelFactory eventFactory =
                new AddScheduledEventViewModelFactory(mDatabase, Calendar.getInstance().getTimeInMillis());
        AddScheduledEventViewModel addScheduledEventViewModel =
                ViewModelProviders.of(this, eventFactory).get(AddScheduledEventViewModel.class);
        //attaches listener to events list
        addScheduledEventViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                //updates the events that are given to mScheduler
                if(mScheduler == null){
                    mScheduler = new Scheduler(events, Calendar.getInstance(), null);
                } else mScheduler.setEvents(events);
            }
        });
    }
    //populates UI given an event
    private void populateUI(Event event){
        mTitle.setText(event.getTitle()); //sets title to event title
        mDescription.setText(event.getDescription());
    }

    //called when the activity closes
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //saves mEventId and the time in milliseconds by putting it in a bundle
        outState.putInt(INSTANCE_EVENT_ID, mEventId);
        outState.putLong(INSTANCE_TIME, mDueDateCalendar.getTimeInMillis());
        super.onSaveInstanceState(outState);
    }

    //intializes each view by matching it to the corresponding view in the layout file
    private void initViews(){
        mTitle = (EditText) findViewById(R.id.scheduled_event_title);
        mDescription = (EditText) findViewById(R.id.scheduled_event_description);
        mHour = (EditText) findViewById(R.id.scheduled_event_time_hour);
        mMinute = (EditText) findViewById(R.id.scheduled_event_time_minutes);
        mCategorySpinner = (Spinner) findViewById(R.id.scheduled_spinner_categories);
        mDueDate = (TextView) findViewById(R.id.tv_scheduled_event_due_date);
        mDueTime = (TextView) findViewById(R.id.tv_scheduled_event_due_time);
    }
    //sets up the mCategorySpinner
    private void setUpSpinner(){
        //instantiates a new CategoryAdapter with the categories from the database
        CategorySpinnerAdapter mCategorySpinnerAdapter = new CategorySpinnerAdapter(this, mCategories);
        mCategorySpinner.setAdapter(mCategorySpinnerAdapter);
        //callback for when an item is selected
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //sets the on click listeners for date/time views
    //when mDueDate or mDueTime is clicked, it will open a date or time picker dialogue
    private void setOnClickListeners(){
        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(mDueDateCalendar);
            }
        });
        mDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(mDueDateCalendar);
            }
        });
    }

    //opens a dialog and saves the picked time into initDate
    private void pickDate(final Calendar initDate){
        //callback for when a date is picked
        DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                initDate.set(year, month, dayOfMonth,
                        initDate.get(Calendar.HOUR_OF_DAY),
                        initDate.get(Calendar.MINUTE),
                        initDate.get(Calendar.SECOND));
                setDateTimeViews();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                setDate, //callback
                initDate.get(Calendar.YEAR), //initial values
                initDate.get(Calendar.MONTH),
                initDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(); //shows the dialogue
    }

    //see above method. Does the same thing but for time
    private void pickTime(final Calendar initTime){
        //callback once time is selected
        TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                initTime.set(
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
                setTime,
                initTime.get(Calendar.HOUR_OF_DAY),
                initTime.get(Calendar.MINUTE),
                false);
        timePickerDialog.show(); //shows the dialogue
    }

    //called to update UI after date and/or time is picked
    private void setDateTimeViews(){
        setDueDateView();
        setDueTimeView();
    }
    //formats string and updates UI
    private void setDueDateView(){
        mDueDate.setText(mDateFormatter.format(mDueDateCalendar.getTime()));
    }
    //formats string and updates UI
    private void setDueTimeView(){
        mDueTime.setText(mTimeFormatter.format(mDueDateCalendar.getTime()));
    }
    //inflates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }
    //when the add category button is clicked
    public void onAddCategoryButtonClicked(View v){
        //starts addcategory activity
        Intent addCategoryIntent = new Intent(this, AddCategoryActivity.class);
        startActivity(addCategoryIntent);
    }
    //when the save button is clicked
    public void onSaveButtonClicked(MenuItem item){
        //gets the title from mTitle
        String title = mTitle.getText().toString();
        //gets description from mDescription
        String description = mDescription.getText().toString();
        //uses try and catch to parse the mHour and mMinute edit text into a long
        long hour ;
        try {
            //mHour to hour long
            hour = TimeUnit.HOURS.toMillis(Long.parseLong(mHour.getText().toString()));
        } catch (Exception e){
            hour = 0; //if cant parse, long hour = 0
        }
        long minute;
        try {
            //mMinute to long minute
            minute = TimeUnit.MINUTES.toMillis(Long.parseLong(mMinute.getText().toString()));
        } catch (Exception e){
            minute = 0; //if can't parse, long hour = 0
        }
        //finds total time length
        timeLengthInMillis = hour + minute;
        //gets the selected category
        Category category = mCategories.get(mCategorySpinner.getSelectedItemPosition());
        //Creates an event with values so far
        final Event event = new Event(title, description, null, null, category);
        //sets the start and end time of the calendar
        mScheduler.setStartCalendar(Calendar.getInstance());
        mScheduler.setEndCalendar(mDueDateCalendar); //the end time is the due date the user entered
        //creates a toast if scheduler fails
        final Toast toast = Toast.makeText(getApplicationContext(), "No Available Time for Event!", Toast.LENGTH_LONG);
        //uses AppExecutors to run database transactions on a separate thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //gets events from the database
                List<Event> events = mDatabase.eventDao().getEventsFromToTimeList(
                        Calendar.getInstance().getTimeInMillis(), mDueDateCalendar.getTimeInMillis());
                mScheduler.setEvents(events); //sets events to mScheduler
                Event eventWithTime = mScheduler.scheduleEvent(event, timeLengthInMillis);
//                //if an event couldn't be scheduled
                if(eventWithTime == null){
                    toast.show();
                }
                //if a new event is being created
                else if (mEventId == DEFAULT_EVENT_ID) {
                    // insert new event
                    mDatabase.eventDao().insertEvent(eventWithTime);
                } else {
                    //update event
                    eventWithTime.setId(mEventId);
                    mDatabase.eventDao().updateEvent(eventWithTime);
                }
                finish(); //closes activity
            }

        });
    }
    //if the delete button is pressed, deletes the event
    public void onDeleteButtonClicked(MenuItem item){
        if(mEventId == DEFAULT_EVENT_ID){
            finish(); //closes activity
        }
        else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.eventDao().deleteEvent(mEvent);
                    finish(); //deletes event then closes the activity
                }
            });
        }
    }
}
