package com.inceptionlabs.restaraunt.Activities;



import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.inceptionlabs.restaraunt.Adapters.LeftNavAdapter;
import com.inceptionlabs.restaraunt.Adapters.MenuPagerAdapter;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.CircularTextView;
import com.inceptionlabs.restaraunt.DataModels.rest_main;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    /** ListView for left side drawer. */
    private View drawerLeft;

    /** The drawer toggle. */
    private ActionBarDrawerToggle drawerToggle;

    private LeftNavAdapter adapter;

    /** The adapter for left navigation ListView. */
    // private LeftNavAdapter adapter;

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private MenuPagerAdapter mPagerAdapter;



    public static FrameLayout fab;
    private TabLayout tabLayout;

    private static CircularTextView counter ;
    public static Menu inst;

    private ArrayList<rest_main> al;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        al = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        drawerLeft = findViewById(R.id.left);

        inst = this;

        ImageView bowl = (ImageView) findViewById(R.id.fab);

        counter = (CircularTextView) findViewById(R.id.cart_counter);
        counter.setSolidColor(Color.parseColor("#ff054c"));

        // bowl.setColorFilter(Color.BLACK);
        fab = (FrameLayout) findViewById(R.id.cart_counter_lay);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total = 0;

                for(int i = 0; i<MainActivity.savedcart.size();i++)
                {
                    saved_cart cart = MainActivity.savedcart.get(i);

                    int quant = Integer.parseInt(cart.getQuant());
                    Float price = Float.parseFloat(cart.getPrice());

                    total+=quant*price;
                    // total_quant+=quant;

                }

                if(total >= Integer.parseInt(MainActivity.min_bill_amt)) {
                    Intent i = new Intent(Menu.this, User_cart.class);

                    startActivity(i);
                    finish();
                }
                else {
                    Toast t =  Toast.makeText(Menu.this, "Add more items worth Rs "+String.valueOf(Integer.parseInt(MainActivity.min_bill_amt) - total)+" for placing order",Toast.LENGTH_LONG);

                    t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                    t.show();
                }
            }
        });

        if(MainActivity.savedcart.size()>0){
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.GONE);
        }

        setupDrawer();



        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.home_hamburger);

         tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mPager = (ViewPager) findViewById(R.id.pager);


       // mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Instantiate a ViewPager and a SliderPagerAdapter.

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                //setupLeftNavDrawer(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        getcategories();



    }

    public void getcategories()
    {

        Globals g = Globals.getInstance();
        String url = "http://"+g.getIp()+"/"+g.getDirectory()+"/get_categories.php";

      //  final LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
      //  linlaHeaderProgress.setVisibility(View.VISIBLE);


        JSONObject jj = new JSONObject();

        try {
            jj.put("rest_id",getIntent().getStringExtra("rest_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest strReq = new JsonObjectRequest(
                url,jj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                JSONArray categories = null;
                try {
                    categories = response.getJSONArray("categories");

                    for (int i = 0; i < categories.length(); i++) {

                        JSONObject jj = (JSONObject) categories.get(i);

                        rest_main cat = new rest_main();
                        cat.setRest_id(jj.getString("cat_id"));
                        cat.setRest_image( " ");
                        cat.setRest_name(jj.getString("name"));




                        tabLayout.addTab(tabLayout.newTab().setText(jj.getString("name")));
                        al.add(cat);
                    }

                    mPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), al.size(), al);
                    mPager.setAdapter(mPagerAdapter);
                    mPager.setOffscreenPageLimit(al.size());
                    setupLeftNavDrawer(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(" ", "Error: " + error.getMessage());
                   // linlaHeaderProgress.setVisibility(View.GONE);
                    Toast.makeText(Menu.this,"error please try again later",Toast.LENGTH_SHORT).show();
                    //  pDialog.hide();
                }
            }) {


            };

// Adding request to request queue
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(strReq);
    }


    @Override
    protected void onResume() {
        super.onResume();

        updatefab();

    }

    public static void updatefab()


    {

        if(MainActivity.savedcart.size()==0)
        {
            fab.setVisibility(View.GONE);
        }
        if( MainActivity.savedcart.size()>0) {
            fab.setVisibility(View.VISIBLE);

            int total_quant = 0;


            for (int i = 0; i < MainActivity.savedcart.size(); i++) {
                saved_cart cart = MainActivity.savedcart.get(i);

                int quant = Integer.parseInt(cart.getQuant());
                Float price = Float.parseFloat(cart.getPrice());


                total_quant += quant;

            }

            counter.setText(String.valueOf(total_quant));
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            DrawerLayout drawer = (DrawerLayout)
                    findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.home_hamburger, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View view)

            {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //setActionBarTitle();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawers();


    }

    private void setupLeftNavDrawer( int position) {

        ListView leeftList = (ListView) findViewById(R.id.left_drawer);

        for(rest_main d : al)
            d.setSelcted(false);
        rest_main selec = al.get(position);
        selec.setSelcted(true);
        adapter = new LeftNavAdapter(Menu.this, al);
        leeftList.setAdapter(adapter);
        leeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                drawerLayout.closeDrawers();
                for (rest_main d : al)
                    d.setSelcted(false);
                al.get(pos).setSelcted(true);
                mPager.setCurrentItem(pos);

                adapter.notifyDataSetChanged();


            }
        });
        leeftList.setSelection(position);

    }

    public void fin(View view) {
        finish();
    }
}
