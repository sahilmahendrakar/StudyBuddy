package com.studybuddy.sahilmahendrakar.studybuddy.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.fragments.DayHolderFragment;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.ResUtil;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.MainViewModel;

import java.util.Calendar;
import java.util.List;

//Initial activity that is first run when the app is opened.
//Holds a floating action button that opens up different activity.
//layout file: activity_main.xml
public class MainActivity extends AppCompatActivity {
    //tag for debugging
    private static final String TAG = MainActivity.class.getSimpleName();
    //Calendar of the current day
    private Calendar mCurrentDay;
    //fragment to be attached to the main activity
    private Fragment mFragment;

    //called when the MainActivity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //inflates layout
        //initializes the ResUtil class
        ResUtil.init(this);
        //intializes mCurrentDay
        mCurrentDay = Calendar.getInstance();
        //sets up view models
        setupViewModel();
        //sets up fragment
        setupFragment();
        setUpFab(); //sets up floating action button
    }

    //sets up the expanding floating action button to have three different buttons when clicked
    private void setUpFab(){
        //button that opens the add assignment activity
        final FloatingActionButton addAssignment = (FloatingActionButton) findViewById(R.id.fab_add_assignment);
        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //callback when button is clicked
                Intent intent = new Intent(MainActivity.this, AddAssignmentActivity.class);
                startActivity(intent); //opens addassignmentactviity
            }
        });
        //button that opens the event activity
        final FloatingActionButton addEvent = (FloatingActionButton) findViewById(R.id.fab_add_event);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //callback when button is clicked
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent); //opens event activity
            }
        });
        //button that opens the add category activity
        FloatingActionButton addCategory = (FloatingActionButton) findViewById(R.id.fab_add_category);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //callback when button is clicked
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent); //opens add category activity
            }
        });
    }

    //attaches a day holder fragment to the Main Activity
    private void setupFragment(){
        //creates a DayHolderFragment with the current time
        DayHolderFragment dayHolderFragment = DayHolderFragment.newInstance(mCurrentDay.getTimeInMillis());
        mFragment = dayHolderFragment; //sets it to mFragment
        //gets a fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //attaches the DayHolderFragment to the main activity
        fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, dayHolderFragment) //using fragment_holder layout
                .commit();
    }
    //sets up view model
    private void setupViewModel(){
        //gets view model for activity
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //attaches a listener for when events are changed in the database
        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel: " + events.toString());
                //prints message to logger
            }
        });
    }
}
