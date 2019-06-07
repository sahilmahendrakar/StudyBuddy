package com.studybuddy.sahilmahendrakar.studybuddy.adapters;

import com.studybuddy.sahilmahendrakar.studybuddy.fragments.DayFragment;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
//Provides an adapter for DayFragments. Used by DayFragmentHolder
public class DayPagerAdapter extends FragmentStatePagerAdapter {
    //number of day fragments the dayholder will hold
    private static int NUM_ITEMS = 365;
    //timestamp of the first day
    private long startDay;

    //constructor
    public DayPagerAdapter(@NonNull FragmentManager fm, long timeInMillis) {
        super(fm);
        startDay = timeInMillis;
    }

    //creates day fragments to populate the view pager
    @NonNull
    @Override
    public Fragment getItem(int position) {
        //finds time in milliseconds of the start of the day
        long time = startDay + TimeUnit.DAYS.toMillis(1)*(position-233);
        //creates dayfragment
        return DayFragment.newInstance(time);

    }
    //gets count of items in view pager
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
