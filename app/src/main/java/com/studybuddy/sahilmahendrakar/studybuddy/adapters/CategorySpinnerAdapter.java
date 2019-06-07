package com.studybuddy.sahilmahendrakar.studybuddy.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;
import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.CategoryColorHolder;

import java.util.ArrayList;

//Adapter for categories. Converts Categories to views for a spinner
public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    private static CategoryColorHolder categoryColorHolder; //used to get colors
    public CategorySpinnerAdapter(Context context, ArrayList<Category> categoryList){
        super(context, 0, categoryList); //gives categoryList to super
        categoryColorHolder = CategoryColorHolder.getInstance();
    }

    //called when views are being inflated
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    //called when views are being inflated
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    //initializes view
    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            //gets layout and inflates it
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item_category, parent, false);
        }
        //assigns views to change later
        ImageView colorCircle = convertView.findViewById(R.id.category_spinner_item_circle_color);
        TextView categoryView = convertView.findViewById(R.id.category_spinner_item_text);
        //gets the current Category object that is being initialized into a view
        Category currentItem = getItem(position);
        if(currentItem != null) {
            GradientDrawable circle = (GradientDrawable) colorCircle.getBackground();
            //changes circle color
            circle.setColor(currentItem.getColor());
            //sets title of the view
            categoryView.setText(currentItem.getTitle());
        }
        //returns new view
        return convertView;
    }
}
