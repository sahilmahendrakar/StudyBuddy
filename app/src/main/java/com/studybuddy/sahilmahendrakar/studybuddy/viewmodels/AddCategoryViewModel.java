package com.studybuddy.sahilmahendrakar.studybuddy.viewmodels;

import android.app.Application;

import com.studybuddy.sahilmahendrakar.studybuddy.database.EventDatabase;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
//view model for addcategoryactivity. Gets categories from events
public class AddCategoryViewModel extends AndroidViewModel {
    private LiveData<List<Category>> categories; //categories from database
    //constructor, gets categories from database
    public AddCategoryViewModel(@NonNull Application application) {
        super(application);
        EventDatabase database = EventDatabase.getInstance(this.getApplication());
        categories = database.categoryDao().getAllCategories();
    }
    //returns categories
    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
