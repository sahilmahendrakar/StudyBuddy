package com.studybuddy.sahilmahendrakar.studybuddy.utilities;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

//Provides access to resources in classes that do not have access to the application context
public class ResUtil {
    //instance, singleton design
    private static ResUtil mInstance;
    //application context to get resources
    private Context mContext;
    //constructor
    private ResUtil(Context context) {
        mContext = context;
    }
    //initializes the resutil and gives a context
    public static void init(Context context) {
        mInstance = new ResUtil(context.getApplicationContext());
    }
    //returns instance
    public static ResUtil getInstance() {
        return mInstance;
    }
    //gets string from resources with an id
    public String getString(@StringRes int id) {
        return mContext.getString(id);
    }
    //gets color from resources with an id
    public int getColor(@ColorRes int id){
        return ContextCompat.getColor(mContext, id);
    }
}