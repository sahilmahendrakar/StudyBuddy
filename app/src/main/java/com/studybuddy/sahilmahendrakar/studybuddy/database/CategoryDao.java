package com.studybuddy.sahilmahendrakar.studybuddy.database;

import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

//Interface for accessing category table in database
@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM category WHERE id = :id")
    LiveData<List<Category>> getCategoryWithId(int id);

    @Query("SELECT * FROM category WHERE title = :title")
    LiveData<List<Category>> getCategoryWithTitle(String title);

    @Query("DELETE FROM category")
    void deleteAll();

    @Insert
    void insertCategory(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);
}
