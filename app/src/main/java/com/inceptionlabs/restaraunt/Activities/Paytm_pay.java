package com.inceptionlabs.restaraunt.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Paytm_pay extends AppCompatActivity {

    MyTextView pay;

    TextView mobile_txt ;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_pay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mobile_txt = (TextView) findViewById(R.id.mobile_id);

        pay = (MyTextView) findViewById(R.id.pay);
        pay.setText(String.format("%.2f", getIntent().getDoubleExtra("pay", 0)));

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);


        pay = (MyTextView) findViewById(R.id.pay);
        pay.setText(String.format("%.2f", getIntent().getDoubleExtra("pay", 0)));

        get_canteen_mobile();

    }

    private void get_canteen_mobile()
    {
        JSONObject job = new JSONObject();

        try {
            job.put("canteen_id" , MainActivity.sel_canteen_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jobreq = new JsonObjectRequest("http://"+ Globals.getInstance().getIp()+"/foodexpress/get_mobile.php", job, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                   String mob =  response.getJSONObject("key").getString("mobile");

                    mobile_txt.setText(mob);
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


        AppController.getInstance().addToRequestQueue(jobreq);



    }

    public void selectaddress(View view) {



        SharedPreferences sp = getSharedPreferences("user_info" , MODE_PRIVATE);
        Gson gson = new Gson();
        String ItemsJson = gson.toJson(MainActivity.savedcart);
        System.out.println(ItemsJson);

        JSONObject Parent = new JSONObject();
        try {

            Parent.put("order",ItemsJson);
            Parent.put("billtotal",getIntent().getDoubleExtra("bill", 0));
            Parent.put("grandtotal",getIntent().getDoubleExtra("pay", 0));
            Parent.put("user_email" , sp.getString("email", ""));
            Parent.put("canteen_id" , MainActivity.sel_canteen_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(Parent);

        JsonObjectRequest jobreq = new JsonObjectRequest("http://" + Globals.getInstance().getIp() + "/foodexpress/addorder.php",Parent , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);

                try {
                    if(response.getString("key").equals("done")) {
                        Toast.makeText(Paytm_pay.this, "order placed successfully", Toast.LENGTH_SHORT).show();

                        MainActivity.savedcart.clear();

                    }
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

        jobreq.setRetryPolicy(new DefaultRetryPolicy(20000 , 2, 2));
        AppController.getInstance().addToRequestQueue(jobreq);
    }
}
