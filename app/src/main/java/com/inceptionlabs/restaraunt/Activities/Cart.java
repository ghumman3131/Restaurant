package com.inceptionlabs.restaraunt.Activities;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.inceptionlabs.restaraunt.R;

public class Cart extends FragmentActivity  {


    TextView quantitytext;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


    }
}

