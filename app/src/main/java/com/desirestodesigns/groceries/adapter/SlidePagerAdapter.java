package com.desirestodesigns.groceries.adapter;

import com.desirestodesigns.groceries.fragment.FirstFragment;
import com.desirestodesigns.groceries.fragment.SecondFragment;
import com.desirestodesigns.groceries.fragment.ThirdFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SlidePagerAdapter extends FragmentPagerAdapter
{


    private static final String TAG = "SLIDE PAGER ADAPTER";

    public SlidePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new FirstFragment();
            case  1:
                return new SecondFragment();
            case 2:
                return  new ThirdFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
