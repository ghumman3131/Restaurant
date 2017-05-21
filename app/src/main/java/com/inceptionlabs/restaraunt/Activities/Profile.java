package com.inceptionlabs.restaraunt.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    private EditText user_name , user_email , user_password , user_mobile;
    private ProgressBar progressBar;
    private RelativeLayout save_profile;
    private ImageView check_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        save_profile = (RelativeLayout) findViewById(R.id.save_profile);

        check_image = (ImageView) findViewById(R.id.check_image);
        check_image.setVisibility(View.GONE);


        user_name = (EditText) findViewById(R.id.user_name);
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        user_mobile = (EditText) findViewById(R.id.user_mobile);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.arrow_back));
        actionBar.setDisplayShowTitleEnabled(false);

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateinfo();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);



        getuserinfo();
    }

    public void getuserinfo()
    {
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id = sp.getString("user_id", "");

        Globals g = Globals.getInstance();
        String url = "http://" + g.getIp() + "/"+g.getDirectory()+"/get_userinfo.php";

        JSONObject jj = new JSONObject();

        try {
            SharedPreferences editor = getSharedPreferences("user_info", MODE_PRIVATE);

            String email = editor.getString("email", "");
            jj.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jj);

        JsonObjectRequest jsobjreq = new JsonObjectRequest(url,jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {


                    System.out.println(response);
                        JSONObject jj = response.getJSONObject("key");
                        user_name.setText(jj.getString("name"));
                        user_email.setText(jj.getString("email"));
                        user_password.setText(jj.getString("password"));
                        user_mobile.setText(jj.getString("mobile"));
                        user_email.setEnabled(false);



                    check_image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

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

    AppController.getInstance().addToRequestQueue(jsobjreq);


}

    public void updateinfo()
    {

        check_image.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Globals g = Globals.getInstance();
        String url = "http://" + g.getIp() + "/foodexpress/update_userinfo.php";

        JSONObject jj = new JSONObject();

        try {
            SharedPreferences editor = getSharedPreferences("user_info", MODE_PRIVATE);
            String email = editor.getString("email", "");
            jj.put("email",email);
            jj.put("name",user_name.getText().toString());
            jj.put("password",user_password.getText().toString());
            jj.put("mobile",user_mobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jj);
        JsonObjectRequest jsobjreq = new JsonObjectRequest(url,jj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                   if(response.getString("key").equals("done"))
                   {
                       Toast.makeText(Profile.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
                   }

                    check_image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
                check_image.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        AppController.getInstance().addToRequestQueue(jsobjreq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
