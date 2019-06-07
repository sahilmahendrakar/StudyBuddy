package com.studybuddy.sahilmahendrakar.studybuddy.utilities;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.CategoryColor;

import java.util.ArrayList;

//holds all possible categorycolors that the user can select
public class CategoryColorHolder {
    //arraylist that holds categoryColors
    private ArrayList<CategoryColor> colorList;
    //singleton design so it doesnt waste space
    private static CategoryColorHolder instance;

    //provides indexes for all the colors in case outside classes want to access a color from arraylist
    public static final int RED = 0;
    public static final int PINK = 1;
    public static final int PURPLE = 2;
    public static final int BLUE = 3;
    public static final int LIGHT_BLUE = 4;
    public static final int GREEN = 5;
    public static final int YELLOW = 6;
    public static final int ORANGE = 7;
    public static final int BROWN = 8;
    public static final int GREY = 9;

    //returns an instance
    public static CategoryColorHolder getInstance(){
        if(instance == null)
            //if there isn't alread an instance, it creates it
            instance = new CategoryColorHolder();
        return instance;
    }
    //constructor
    private CategoryColorHolder(){
        ResUtil resUtil = ResUtil.getInstance(); //uses resutil to get colors
        //fills arraylist with colors
        colorList = new ArrayList<>();
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_red), resUtil.getColor(R.color.categoryColorRed)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_pink), resUtil.getColor(R.color.categoryColorPink)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_purple), resUtil.getColor(R.color.categoryColorPurple)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_blue), resUtil.getColor(R.color.categoryColorBlue)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_light_blue), resUtil.getColor(R.color.categoryColorLightBlue)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_green), resUtil.getColor(R.color.categoryColorGreen)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_yellow), resUtil.getColor(R.color.categoryColorYellow)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_orange), resUtil.getColor(R.color.categoryColorOrange)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_brown), resUtil.getColor(R.color.categoryColorBrown)));
        colorList.add(new CategoryColor(resUtil.getString(R.string.category_color_grey), resUtil.getColor(R.color.categoryColorGrey)));
    }
    //returns the int of the color
    public int getColorInt(int index){
        return colorList.get(index).getColorInt();
    }

    //returns category color
    public CategoryColor getCategoryColor(int index){
        return colorList.get(index);
    }

    //returns the entire list
    public ArrayList<CategoryColor> getColorList(){
        return colorList;
    }

    //finds the index from a color int
    public int getIndexFromColorInt(int color){
        for(int i = 0; i < colorList.size(); i++){
            CategoryColor item = colorList.get(i);
            if(color == item.getColorInt()){
                return i;
            }
        }
        return 0;
    }

}
