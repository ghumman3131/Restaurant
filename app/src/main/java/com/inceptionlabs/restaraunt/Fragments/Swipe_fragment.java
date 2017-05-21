package com.inceptionlabs.restaraunt.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import java.util.ArrayList;

/**
 * Created by ghumman on 5/12/2016.
 */
public class Swipe_fragment extends Fragment {




    NetworkImageView imageView;

    int position = 0;
    ArrayList<String> images ;
    ImageLoader  imgload = AppController.getInstance().getImageLoader();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View swipeView = inflater.inflate(R.layout.slider, container, false);
        imageView = (NetworkImageView) swipeView.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        images = new ArrayList<>();
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        ArrayList<String> images = bundle.getStringArrayList("images");

        imageView.setImageUrl(images.get(position),imgload);



        return swipeView;
    }

    public static Swipe_fragment newInstance(int position , ArrayList<String> images) {
        Swipe_fragment swipeFragment = new Swipe_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArrayList("images",images);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }

    @Override
    public void onResume() {

        Globals g = Globals.getInstance();

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        ArrayList<String> images = bundle.getStringArrayList("images");

        imageView.setImageUrl("http://"+g.getIp()+"/"+g.getDirectory()+"/images/"+images.get(position),imgload);
        super.onResume();
    }
}

