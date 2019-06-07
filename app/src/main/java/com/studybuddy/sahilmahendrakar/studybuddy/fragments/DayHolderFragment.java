package com.studybuddy.sahilmahendrakar.studybuddy.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studybuddy.sahilmahendrakar.studybuddy.R;
import com.studybuddy.sahilmahendrakar.studybuddy.adapters.DayPagerAdapter;

import java.util.Calendar;

public class DayHolderFragment extends Fragment {

    private static String START_TIME = "start_time";

    private ViewPager mViewPager;
    private Calendar mStartCalendar;

    public static DayHolderFragment newInstance(long startTime){
        Bundle bundle = new Bundle();
        bundle.putLong(START_TIME, startTime);

        DayHolderFragment fragment = new DayHolderFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public DayHolderFragment() {
        // Required empty public constructor
    }

    private void readBundle(Bundle bundle){
        mStartCalendar = Calendar.getInstance();
        mStartCalendar.setTimeInMillis(bundle.getLong(START_TIME));
        mStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mStartCalendar.set(Calendar.MINUTE, 0);
        mStartCalendar.set(Calendar.SECOND, 0);
        mStartCalendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            readBundle(getArguments());
        }
        return inflater.inflate(R.layout.day_holder_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_day_holder);
        DayPagerAdapter dayAdapter = new DayPagerAdapter(
                getChildFragmentManager(), mStartCalendar.getTimeInMillis());
        mViewPager.setAdapter(dayAdapter);
        mViewPager.setCurrentItem(233);
    }

}
