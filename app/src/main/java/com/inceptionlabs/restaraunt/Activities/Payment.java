package com.inceptionlabs.restaraunt.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment extends AppCompatActivity {

    MyTextView pay;
    public static Payment inst;
    private EditText cardname, cardnumber, carddate, cardcsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        inst = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pay = (MyTextView) findViewById(R.id.pay);
        pay.setText(String.format("%.2f", getIntent().getDoubleExtra("pay", 0)));

        cardname = (EditText) findViewById(R.id.cardname);
        cardnumber = (EditText) findViewById(R.id.cardnumber);
        carddate = (EditText) findViewById(R.id.carddate);
        cardcsc = (EditText) findViewById(R.id.cardcsc);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectaddress(View view) {

        if (cardname.getText().toString().equals("") || cardnumber.getText().toString().equals("") || carddate.getText().toString().equals("") || cardcsc.getText().toString().equals("")) {
            Toast.makeText(Payment.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            if (cardnumber.getText().toString().length() == 16) {
                if (cardcsc.getText().toString().length() == 3) {

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

                            try {
                                if(response.getString("key").equals("done")) {
                                    Toast.makeText(Payment.this, "order placed successfully", Toast.LENGTH_SHORT).show();

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

                } else {
                    Toast.makeText(Payment.this, "invalid cvv number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Payment.this, "invalid card number", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
