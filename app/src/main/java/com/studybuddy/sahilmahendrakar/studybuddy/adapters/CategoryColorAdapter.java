package com.studybuddy.sahilmahendrakar.studybuddy.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.CategoryColor;
import com.studybuddy.sahilmahendrakar.studybuddy.utilities.CategoryColorHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//Provides an adapter for category colors
public class CategoryColorAdapter extends ArrayAdapter<CategoryColor> {
    private CategoryColorHolder categoryColorHolder; //Uses this to get category colors

    //constructor
    public CategoryColorAdapter(Context context, ArrayList<CategoryColor> colorList){
        super(context, 0, colorList); //gives colorList to super class
        categoryColorHolder = CategoryColorHolder.getInstance();
    }

    //called when adapter is being used
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    //called when adapter is being used
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    //converts a CategoryColor into a view for display in a spinner
    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            //inflates layout view for category color
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item_category_color, parent, false);
        }
        //assigns views so that it can set them
        ImageView colorCircle = convertView.findViewById(R.id.category_color_spinner_item_circle_color);
        TextView colorTextView = convertView.findViewById(R.id.category_color_spinner_item_text);
        //gets current Category color that is being converted
        CategoryColor currentItem = getItem(position);
        if(currentItem != null) {
            GradientDrawable circle = (GradientDrawable) colorCircle.getBackground();
            //sets the color of the circle to the color of the categorycolor
            circle.setColor(currentItem.getColorInt());
            //sets the text and text color
            colorTextView.setText(currentItem.getColorName());
            colorTextView.setTextColor(currentItem.getColorInt());
        }
        return convertView;
    }
}