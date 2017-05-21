package com.inceptionlabs.restaraunt.Adapters;


import android.support.v4.app.FragmentPagerAdapter;

import com.inceptionlabs.restaraunt.Fragments.Swipe_fragment;

import java.util.ArrayList;

public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> img;
    int count;

    public ImageFragmentPagerAdapter(android.support.v4.app.FragmentManager fm , ArrayList<String> img , int count) {


        super(fm);
        this.img = img;
        this.count = count;
    }



    @Override
    public android.support.v4.app.Fragment getItem(int position) {




        return Swipe_fragment.newInstance(position,img);
    }

    @Override
    public int getCount() {
        return count;
    }
}
