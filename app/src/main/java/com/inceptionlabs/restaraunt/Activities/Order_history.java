package com.inceptionlabs.restaraunt.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.inceptionlabs.restaraunt.Adapters.order_history_adapter;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Order_history extends AppCompatActivity {

    private LinearLayout orderslay;
    private Button noorders;
    private MyTextView orderid , pay , bill , date , discount;

    RecyclerView order_history_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        order_history_rec = (RecyclerView) findViewById(R.id.order_history);
        order_history_rec.setLayoutManager(new LinearLayoutManager(Order_history.this , LinearLayoutManager.VERTICAL,false));


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);




        get_orders();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void get_orders()
    {
        SharedPreferences sp = getSharedPreferences("user_info" , MODE_PRIVATE);


        JSONObject job = new JSONObject();

        try {
            job.put("user_email" , sp.getString("email" ,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(job);

        JsonObjectRequest jobreq = new JsonObjectRequest("http://" + Globals.getInstance().getIp() + "/foodexpress/get_orders.php", job, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    System.out.println(response);
                    JSONArray jarr = response.getJSONArray("result");

                    order_history_adapter ad = new order_history_adapter(jarr , Order_history.this);

                    order_history_rec.setAdapter(ad);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        jobreq.setRetryPolicy(new DefaultRetryPolicy(20000 , 2 , 2));

        AppController.getInstance().addToRequestQueue(jobreq);
    }
}
