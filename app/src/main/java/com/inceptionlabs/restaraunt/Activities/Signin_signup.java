package com.inceptionlabs.restaraunt.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

/**
 * Created by ghumman on 11/18/2016.
 */


public class Signin_signup extends AppCompatActivity implements View.OnClickListener , Animation.AnimationListener {

    private EditText sign_in_email , sign_in_password , register_email , register_mobile, register_name , register_pass , register_repeat_pass;
    private Button sign_in_btn , register_btn;
    private MyTextView create_account , already_account;
    private Boolean signup = false;
    private LinearLayout signin_lay , register_lay;
    private Animation fadein , fadeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signin_signup_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);

        fadein = AnimationUtils.loadAnimation(this,R.anim.fadein);
        fadeout = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        fadein.setAnimationListener(this);
        fadeout.setAnimationListener(this);

        signup = false;
        sign_in_email = (EditText) findViewById(R.id.signin_email);
        sign_in_password = (EditText) findViewById(R.id.signin_password);
        register_email = (EditText) findViewById(R.id.register_email);
        register_name = (EditText) findViewById(R.id.register_name);
        register_pass = (EditText) findViewById(R.id.register_password);
        register_repeat_pass = (EditText) findViewById(R.id.register_repeat_password);
        register_mobile = (EditText) findViewById(R.id.register_mobile);


        signin_lay = (LinearLayout) findViewById(R.id.signin_lay);
        register_lay = (LinearLayout)  findViewById(R.id.register_lay);

        sign_in_btn = (Button) findViewById(R.id.sign_in_btn);
        register_btn = (Button) findViewById(R.id.register_btn);

        create_account = (MyTextView) findViewById(R.id.create_new_acc);
        already_account = (MyTextView) findViewById(R.id.already_acc);

        create_account.setOnClickListener(this);
        already_account.setOnClickListener(this);

        register_btn.setOnClickListener(this);
        sign_in_btn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.create_new_acc:
                if(!signup)
                {
                    signup = true;
                    signin_lay.startAnimation(fadeout);


                }

                break;

            case R.id.already_acc:
                if(signup)
                {

                    signup = false;
                    register_lay.startAnimation(fadeout);
                }
                break;

            case R.id.register_btn:
                registeruser();
                break;

            case R.id.sign_in_btn:
                login();

        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if(signup && animation==fadeout)
        {
            signin_lay.setVisibility(View.GONE);
            register_lay.setVisibility(View.VISIBLE);
            register_lay.startAnimation(fadein);
        }
        if(signup && animation == fadein)
        {
            register_lay.setVisibility(View.VISIBLE);
        }

        if(!signup && animation == fadeout)
        {
            register_lay.setVisibility(View.GONE);
            signin_lay.setVisibility(View.VISIBLE);
            signin_lay.startAnimation(fadein);
        }
        if(!signup && animation ==fadein)
        {
            signin_lay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void registeruser() {
        if (register_email.getText().toString().equals("") || register_mobile.getText().toString().equals("") || register_name.getText().toString().equals("") || register_pass.getText().toString().equals(""))
        {

            Toast.makeText(Signin_signup.this,"please fill all fields",Toast.LENGTH_SHORT).show();
        } else {

            if(Patterns.EMAIL_ADDRESS.matcher(register_email.getText().toString()).matches())
            {

            }
            else {
                Toast.makeText(Signin_signup.this,"please enter valid email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(register_pass.getText().toString().length() < 8)
            {
                Toast.makeText(Signin_signup.this,"please enter atleast 8 character password",Toast.LENGTH_SHORT).show();
                return;

            }
            if (register_pass.getText().toString().equals(register_repeat_pass.getText().toString())) {
                register_btn.setText("PLEASE WAIT");
                register_btn.setEnabled(false);
                Globals g = Globals.getInstance();
                String url = "http://" + g.getIp() + "/foodexpress/create_user_acc.php";

                JSONObject jj = new JSONObject();

                try {
                    jj.put("name",register_name.getText().toString());
                    jj.put("mobile",register_mobile.getText().toString());
                    jj.put("email",register_email.getText().toString());
                    jj.put("password",register_pass.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsobjreq = new JsonObjectRequest(url,jj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getInt("success") == 0) {
                                Toast.makeText(Signin_signup.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                                register_btn.setText("SIGN UP");
                                register_btn.setEnabled(true);
                            } else if (response.getInt("success") == 1) {

                                SharedPreferences.Editor spe = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                                spe.putString("name",register_name.getText().toString());
                                spe.putString("email",register_email.getText().toString());
                                spe.commit();
                                if(getIntent().getStringExtra("from").equals("cart"))
                                {
                                    finish();

                                }
                                else {
                                    startActivity(new Intent(Signin_signup.this,Profile.class));
                                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                                    finish();
                                }


                                register_btn.setText("SIGN UP");
                                register_btn.setEnabled(true);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error);
                        register_btn.setText("SIGN UP");
                        register_btn.setEnabled(true);
                    }
                });

                AppController.getInstance().addToRequestQueue(jsobjreq);

            }
            else {
                Toast.makeText(Signin_signup.this,"password do not matched",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void login()
    {
        if (sign_in_email.getText().toString().equals("") ||  sign_in_password.getText().toString().equals(""))
        {

            Toast.makeText(Signin_signup.this,"please fill all fields",Toast.LENGTH_SHORT).show();
        } else {

            if (Patterns.EMAIL_ADDRESS.matcher(sign_in_email.getText().toString()).matches()) {

                if(sign_in_password.getText().toString().length() < 8)
                {
                    Toast.makeText(Signin_signup.this,"please enter atleast 8 character password",Toast.LENGTH_SHORT).show();
                }
                else {
                    sign_in_btn.setText("PLEASE WAIT");
                    sign_in_btn.setEnabled(false);
                    Globals g = Globals.getInstance();
                    String url = "http://" + g.getIp() + "/foodexpress/user_login.php";

                    JSONObject jj = new JSONObject();

                    try {

                        jj.put("email", sign_in_email.getText().toString());
                        jj.put("password", sign_in_password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsobjreq = new JsonObjectRequest(url, jj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("result").equals("yes")) {


                                    SharedPreferences.Editor spe = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                                    spe.putString("name", response.getString("name"));
                                    spe.putString("email", sign_in_email.getText().toString());
                                    spe.commit();
                                    if (getIntent().getStringExtra("from").equals("cart")) {
                                        finish();

                                    } else {
                                        startActivity(new Intent(Signin_signup.this, Profile.class));
                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        finish();
                                    }


                                    sign_in_btn.setText("SIGN IN");
                                    sign_in_btn.setEnabled(true);

                                } else {
                                    Toast.makeText(Signin_signup.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                                    sign_in_btn.setText("SIGN IN");
                                    sign_in_btn.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            System.out.println(error);
                            sign_in_btn.setText("SIGN IN");
                            sign_in_btn.setEnabled(true);
                        }
                    });

                    AppController.getInstance().addToRequestQueue(jsobjreq);
                }

            } else {
                Toast.makeText(Signin_signup.this,"please enter valid email",Toast.LENGTH_SHORT).show();
            }


        }
    }

}
