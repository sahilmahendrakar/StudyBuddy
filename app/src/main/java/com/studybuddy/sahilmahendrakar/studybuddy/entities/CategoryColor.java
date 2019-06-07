package com.studybuddy.sahilmahendrakar.studybuddy.entities;

//POJO to represent colors of categories
public class CategoryColor {
    private String colorName; //name of color
    private int colorInt; //color stored as an int

    //constructor
    public CategoryColor(String colorName, int colorInt) {
        this.colorName = colorName;
        this.colorInt = colorInt;
    }

    ///getters and setters///
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }
}
