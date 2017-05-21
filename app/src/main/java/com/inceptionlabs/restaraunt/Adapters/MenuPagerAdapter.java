package com.inceptionlabs.restaraunt.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


import com.inceptionlabs.restaraunt.DataModels.rest_main;
import com.inceptionlabs.restaraunt.Fragments.Menu_fragment;

import java.util.List;

/**
 * Created by ghumman on 6/12/2016.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter {

    int count;

    List<rest_main> items;

    public MenuPagerAdapter(android.support.v4.app.FragmentManager fm, int count , List<rest_main> items) {
        super(fm);

        this.count = count;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Menu_fragment();
        Bundle b = new Bundle();
        rest_main selected_item = items.get(position);
        String selected = selected_item.getRest_id();
        b.putString("extra",selected);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }
}

