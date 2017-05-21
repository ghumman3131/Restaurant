package com.inceptionlabs.restaraunt.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inceptionlabs.restaraunt.Adapters.show_pop_cart_adapter;
import com.inceptionlabs.restaraunt.R;


public class user_cart_pop_up extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart_pop_up);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.show_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new show_pop_cart_adapter(this, MainActivity.savedcart));
        this.setFinishOnTouchOutside(false);
    }

    public void close(View view) {

        setResult(3000);

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(3000);

    }
}

