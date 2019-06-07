package com.studybuddy.sahilmahendrakar.studybuddy.entities;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//Category object to represent categories that the user creates
//stored in database table called object
@Entity(tableName = "category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id; //unique id
    private String title; //title of category
    private int color; //color of category

    //constructor
    public Category(int id, String title, int color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    //constructor
    @Ignore
    public Category(String title, int color) {
        this.title = title;
        this.color = color;
    }

    ///getters and setters///
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    //to string method
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", color=" + color +
                '}';
    }

    //compares ids to see if categories are equal
    @Ignore
    public boolean equals(Category category){
        if(this.id == category.id){
            return true;
        } else return false;
    }
}
