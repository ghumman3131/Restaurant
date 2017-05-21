package com.inceptionlabs.restaraunt.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONException;
import org.json.JSONObject;

public class User_cart extends AppCompatActivity {

    private TextView selectaddress2 , no_of_items , total_amount , vat_tax , bill_total , delivery_charges , grand_total , postorderbtn;

    private LinearLayout signin_signup , name_mobile;

    private EditText name_et , phone_et;

    private String address , discount_type = "no";
    private double billtotal , grandtotal , discount , deliverycharges;

    private LinearLayout apply_coupon;

    private RadioButton cashondelivery;

    private double vat = 0;

    private ProgressBar postorderbar;


    public static User_cart inst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inst = this;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);

        no_of_items = (TextView) findViewById(R.id.no_of_items);
        total_amount = (TextView) findViewById(R.id.total_amount);
        vat_tax = (TextView) findViewById(R.id.vat_text);
        bill_total = (TextView) findViewById(R.id.bill_total);

        grand_total = (TextView) findViewById(R.id.grand_total);



        postorderbar = (ProgressBar) findViewById(R.id.postorderbar);
        postorderbtn = (TextView) findViewById(R.id.postorderbtn);

       apply_coupon = (LinearLayout) findViewById(R.id.apply_coupon);

        vat = Integer.parseInt(MainActivity.vat_perc);







        int total_quant = 0;

        Double total = 0.0;

        for(int i = 0; i<MainActivity.savedcart.size();i++)
        {
            saved_cart cart = MainActivity.savedcart.get(i);

            int quant = Integer.parseInt(cart.getQuant());
            Double price = Double.parseDouble(cart.getPrice());

            total+=quant*price;
            total_quant+=quant;

        }

        no_of_items.setText(String.valueOf(total_quant));

        total_amount.setText(String.valueOf(total));





        double vat_total = total * vat /100;

        vat_tax.setText(String.valueOf(vat_total));

        //Toast.makeText(User_cart.this,String.valueOf(vat_total),Toast.LENGTH_SHORT).show();

        billtotal = total + vat_total;


        apply_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_cart.this, ApplyCoupon.class);
                i.putExtra("bill", billtotal);
                startActivityForResult(i, 4);
            }
        });

        bill_total.setText(String.valueOf(billtotal));



       // selectaddress = (TextView) findViewById(R.id.select_address);

        signin_signup = (LinearLayout) findViewById(R.id.sign_in_sign_up);

        name_mobile = (LinearLayout) findViewById(R.id.name_mobile);

        name_et = (EditText) findViewById(R.id.name_et);

        phone_et = (EditText) findViewById(R.id.phone_et);

        if(billtotal < Integer.parseInt(MainActivity.delivery_min))
        {
           // delivery_charges.setText(String.valueOf(deliverycharges));
            //Toast.makeText(User_cart.this,"Add more items worth Rs "+String.valueOf(Integer.parseInt(MainActivity.delivery_min) - billtotal)+" for free delivery",Toast.LENGTH_LONG).show();

            Toast t =  Toast.makeText(User_cart.this, "Add more items worth Rs "+String.valueOf(Integer.parseInt(MainActivity.delivery_min) - billtotal)+" for free delivery",Toast.LENGTH_LONG);

            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            t.show();

        }
        else {
           // delivery_charges.setText("0");
        }

        grandtotal = billtotal - discount + Integer.parseInt("0");

        grand_total.setText(String.format("%.2f", grandtotal));

        SharedPreferences editor = getSharedPreferences("user_details", MODE_PRIVATE);
        String name = editor.getString("name", "");

        String phone = editor.getString("mobile", "");

        if( !name.equals(""))
        {
            signin_signup.setVisibility(View.GONE);
            name_mobile.setVisibility(View.VISIBLE);
            name_et.setText(name);
            name_et.setEnabled(false);
            phone_et.setText(phone);
            phone_et.setEnabled(false);

        }
        else {
            signin_signup.setVisibility(View.VISIBLE);
            name_mobile.setVisibility(View.GONE);

        }

      /*  selectaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("user_details",MODE_PRIVATE);
                String mobile = preferences.getString("mobile","");

                if(mobile.equals(""))
                {
                    Toast.makeText(User_cart.this,"you need to sign in first",Toast.LENGTH_SHORT).show();
                }
                else {
                  /*  Intent i = new Intent(User_cart.this, Address.class);
                    i.putExtra("id", 1);
                    startActivityForResult(i, 3);
                }
            }
        });*/






    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3 && resultCode==2){

            address = data.getStringExtra("address");

           // selectaddress.setText("Address  "+ address);

        }
        if(requestCode==200 && resultCode==300)
        {
            int total_quant = 0;

            Double total = 0.0;

            for(int i = 0; i<MainActivity.savedcart.size();i++)
            {
                saved_cart cart = MainActivity.savedcart.get(i);

                int quant = Integer.parseInt(cart.getQuant());
                Double price = Double.parseDouble(cart.getPrice());

                total+=quant*price;
                total_quant+=quant;

            }

            no_of_items.setText(String.valueOf(total_quant));

            total_amount.setText(String.valueOf(total));





            double vat_total = 0;

            vat_tax.setText(String.valueOf(vat_total));

            //Toast.makeText(User_cart.this,String.valueOf(vat_total),Toast.LENGTH_SHORT).show();

            billtotal = total + vat_total;

            bill_total.setText(String.valueOf(billtotal));
            if(billtotal < Integer.parseInt(MainActivity.delivery_min))
            {
               // delivery_charges.setText(String.valueOf(deliverycharges));
                //  Toast.makeText(User_cart.this,"Add more items worth Rs "+String.valueOf(Integer.parseInt(MainActivity.delivery_min) - billtotal)+" for free delivery",Toast.LENGTH_LONG).show();

                Toast t =  Toast.makeText(User_cart.this, "Add more items worth Rs "+String.valueOf(Integer.parseInt(MainActivity.delivery_min) - billtotal)+" for free delivery",Toast.LENGTH_SHORT);

                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                t.show();


            }
            else {
               // delivery_charges.setText("0");
            }

            discount = 0;
          //  TextView textView = (TextView) findViewById(R.id.discount_text);
          //  textView.setText(String.valueOf(discount));
          //  TextView textView1 = (TextView) findViewById(R.id.apply_text);
          //  textView1.setText("Apply coupon / cashback");

            grandtotal = billtotal-discount + Integer.parseInt("0");

            grand_total.setText(String.format("%.2f",grandtotal));
        }

        if(requestCode == 4 && resultCode == 5)
        {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.discount);
            linearLayout.setVisibility(View.VISIBLE);

            TextView textView = (TextView) findViewById(R.id.discount_text);

            discount = data.getDoubleExtra("discount",0);
            discount_type = "";

            textView.setText(String.format("%.2f", discount));

            TextView textView1 = (TextView) findViewById(R.id.apply_text);
            textView1.setText("Discount");

            grandtotal = billtotal-discount + Integer.parseInt(delivery_charges.getText().toString());

            grand_total.setText(String.format("%.2f", grandtotal));


        }
    }

    public void signin(View view) {

        Intent i = new Intent(User_cart.this,Signin_signup.class);
        i.putExtra("from","cart");
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences editor = getSharedPreferences("user_info", MODE_PRIVATE);
        String name = editor.getString("name", "");

        String email = editor.getString("email", "");

        if( !name.equals(""))
        {
            signin_signup.setVisibility(View.GONE);
            name_mobile.setVisibility(View.VISIBLE);
            name_et.setText(name);
            name_et.setEnabled(false);
            phone_et.setText(email);
            phone_et.setEnabled(false);

        }
        else {
            signin_signup.setVisibility(View.VISIBLE);
            name_mobile.setVisibility(View.GONE);

        }
    }

    public void pop_cart(View view) {

        Intent i = new Intent(User_cart.this , user_cart_pop_up.class);
        startActivityForResult(i,200);
    }


    public void takeorder(View view)
    {

     /*   if(name_mobile.getVisibility() != View.VISIBLE)
        {
            // Toast.makeText(User_cart.this,"please sign in to continue",Toast.LENGTH_SHORT).show();
            Toast t =  Toast.makeText(User_cart.this, "please sign in to continue",Toast.LENGTH_SHORT);

            t.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL,0,500);
            t.show();

            return;
        }
        if(selectaddress.getText().toString().equals("delivery address")) {
            Toast t =  Toast.makeText(User_cart.this, "please select delivery address",Toast.LENGTH_SHORT);

            t.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL,0,500);
            t.show();

        }

        else {*/

           /* MainActivity.showdialog(User_cart.this);

            postorderbtn.setVisibility(View.GONE);
            postorderbar.setVisibility(View.VISIBLE);
            String name = name_et.getText().toString();
            String phone = phone_et.getText().toString();
            String optional = "no";
            EditText optional_phone = (EditText) findViewById(R.id.optional_phone);
            if(!optional_phone.getText().toString().equals(""))
            {
                optional = optional_phone.getText().toString();
            }

            Gson gson = new Gson();
            String ItemsJson = gson.toJson(MainActivity.savedcart);
            System.out.println(ItemsJson);

            JSONObject Parent = new JSONObject();
            try {
                Parent.put("name",name);
                Parent.put("mobile",phone);
                Parent.put("optional",optional);
                Parent.put("address",address);
                Parent.put("order",ItemsJson);
                Parent.put("billtotal",String.valueOf(billtotal));
                Parent.put("grandtotal",String.valueOf(grandtotal));
                Parent.put("discount",discount);
                Parent.put("discount_type",discount_type);
                Parent.put("delivery_charge",delivery_charges.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Globals g = Globals.getInstance();

            String url = "http://"+g.getIp()+"/"+g.getDirectory()+"/postfinalcart.php";

            JsonObjectRequest jobjreq = new JsonObjectRequest(Request.Method.POST, url, Parent, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {
                        if(Integer.parseInt(jsonObject.getString("success"))==1)

                        {
                            Toast.makeText(User_cart.this,"order placed successfully",Toast.LENGTH_SHORT).show();
                            MainActivity.savedcart.clear();
                            MainActivity.hidedialog();
                            finish();

                           // startActivity(new Intent(User_cart.this, My_orders.class));


                        }
                        else
                        {
                            Toast.makeText(User_cart.this,"error please try again",Toast.LENGTH_SHORT).show();
                            MainActivity.hidedialog();
                            postorderbtn.setVisibility(View.VISIBLE);
                            postorderbar.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    MainActivity.hidedialog();
                    postorderbtn.setVisibility(View.VISIBLE);
                    postorderbar.setVisibility(View.GONE);
                }
            });

            jobjreq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jobjreq);




        }*/
    }

    public void makepayment(View view) {

        if(name_mobile.getVisibility() != View.VISIBLE)
        {
            // Toast.makeText(User_cart.this,"please sign in to continue",Toast.LENGTH_SHORT).show();
            Toast t =  Toast.makeText(User_cart.this, "please sign in to continue",Toast.LENGTH_SHORT);

            t.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL,0,500);
            t.show();

            return;
        }


        else {
            Intent i = new Intent(User_cart.this, Payment_options.class);
            i.putExtra("pay", grandtotal);
            i.putExtra("bill", billtotal);
            i.putExtra("discount", discount);

            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}

