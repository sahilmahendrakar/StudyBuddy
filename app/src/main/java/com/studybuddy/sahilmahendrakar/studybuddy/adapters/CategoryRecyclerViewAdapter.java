package com.studybuddy.sahilmahendrakar.studybuddy.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.entities.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//Provides an adapter for categories. Converts categories to views for recyclerView
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {
    private Context mContext;
    //list of categories to be populated in recycler view
    private List<Category> mCategoryList;
    //constructor
    public CategoryRecyclerViewAdapter(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    //called when the view holders are being created
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext) //inflates view
                .inflate(R.layout.category_rv_item, parent, false);
        return new CategoryViewHolder(view); //returns a view holder created from the view
    }

    //called after the view holder is binded to the recycler view
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        //gets a category object from list that it will use to set the view holder
        Category category = mCategoryList.get(position);
        //gets title from category object
        String title = category.getTitle();
        //sets title
        holder.title.setText(title);
        //changes color of circle
        GradientDrawable circle = (GradientDrawable) holder.title.getBackground();
        circle.setColor(category.getColor());
        //holder.title.setBackgroundColor(category.getColor());
    }

    //returns the length of mCategoryList
    @Override
    public int getItemCount() {
        if (mCategoryList == null) {
            return 0;
        }
        return mCategoryList.size();
    }
    //getter
    public List<Category> getCategories(){
        return mCategoryList;
    }
    //setter
    public void setCategories(List<Category> categories) {
        mCategoryList = categories;
        notifyDataSetChanged(); //notifies recyclerview that data has been updated
    }

    //class for view holders that represent Categories
    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView title; //text view for title
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            //assigns title to corresponding layout components
            title = itemView.findViewById(R.id.tv_category_rv_item_title);
        }
    }
}
