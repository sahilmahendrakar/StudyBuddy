package com.studybuddy.sahilmahendrakar.studybuddy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.adapters.CategoryColorAdapter;
import com.studybuddy.sahilmahendrakar.studybuddy.adapters.CategoryRecyclerViewAdapter;
import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.CategoryColor;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Event;
import com.studybuddy.sahilmahendrakar.studybuddy.executors.AppExecutors;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.CategoryColorHolder;
import com.studybuddy.sahilmahendrakar.studybuddy.viewmodels.AddCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
//Provides a user interface to add or remove a category to the database
//layout file: activity_add_category.xml
public class AddCategoryActivity extends AppCompatActivity {

    //edit text for user to enter a title for category
    private EditText mTitle;

    //spinner to display a list of colors the user can choose from
    private ArrayList<CategoryColor> mCategoryColorList;
    private CategoryColorHolder mCategoryColorHolder;
    private Spinner mColorSpinner;

    //recycler view to display categories
    private RecyclerView mRecyclerView;
    private CategoryRecyclerViewAdapter mRvAdapter;

    //event database for adding, removing, or querying events
    private EventDatabase mDatabase;

    //called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category); //inflates layout
        //instantiates the database
        mDatabase = EventDatabase.getInstance(this);

        //assigns the EditText mTitle to its corresponding layout component
        mTitle = findViewById(R.id.category_title);

        //assigns variables necessary for the spinner
        mCategoryColorHolder = CategoryColorHolder.getInstance();
        mColorSpinner = (Spinner) findViewById(R.id.color_spinner);

        //sets up the recycler view to display categories
        mRecyclerView = findViewById(R.id.rv_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRvAdapter = new CategoryRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mRvAdapter);
        //Callback for when categories in recycler view are touched
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //don't do anything when items are moved
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // uses AppExecutors to delete category on a separate thread
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Category> categories = mRvAdapter.getCategories();
                        //removes category from database
                        mDatabase.categoryDao().deleteCategory(categories.get(position));
                    }
                });
            }
            //attaches the touch listener to the Recycler view
        }).attachToRecyclerView(mRecyclerView);
        //sets up view model
        setupViewModel();
        //sets up the color spinner
        setupSpinner();
    }
    //sets up view model
    private void setupViewModel(){
        //instantiates view model
        AddCategoryViewModel viewModel = ViewModelProviders.of(this).get(AddCategoryViewModel.class);
        //attaches view model to update categories of RecyclerView if categories in database changes
        viewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                mRvAdapter.setCategories(categories);
            }
        });
    }

    //sets up color spiiner
    private void setupSpinner(){
        mCategoryColorList = mCategoryColorHolder.getColorList(); //gets all the colors
        CategoryColorAdapter mCategoryColorAdapter = new CategoryColorAdapter(this, mCategoryColorList);

        mColorSpinner.setAdapter(mCategoryColorAdapter);
        //callback for when an item is selected in the spinner
        mColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryColor selectedItem = (CategoryColor) parent.getItemAtPosition(position);
                String categoryName = selectedItem.getColorName(); //gets the name and position of item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //necessary blank override
            }
        });
    }

    //closes the activity when the finished button is clicked
    public void onFinishedMenuButtonClicked(MenuItem menuItem){
        finish();
    }

    //inflates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu); //inflates menue
        return true;
    }

    //when add category button is pressed adds category to database
    public void onAddCategoryButtonClicked(View v){
        //gets title of category
        String title = mTitle.getText().toString();
        //if no title was entered
        if(title.equals("")) {
            Toast.makeText(this, "No title was entered", Toast.LENGTH_SHORT).show();
            return;
        }
        //gets color from spinner
        int color = mCategoryColorHolder.getColorInt(mColorSpinner.getSelectedItemPosition());
        //creates a new Category object from these values
        final Category category = new Category(title, color);
        //adds category to database on a separate thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.categoryDao().insertCategory(category);
            }

        });
    }
}
