package com.inceptionlabs.restaraunt.Activities;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.inceptionlabs.restaraunt.Adapters.ImageFragmentPagerAdapter;
import com.inceptionlabs.restaraunt.Adapters.rest_list_adapter;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.Custom.CircularTextView;
import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.DataModels.rest_main;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.inceptionlabs.restaraunt.R.layout.activity_radio;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;


    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    private RecyclerView cat_main;
    private ArrayList<rest_main> list;
    private rest_list_adapter adapter;

    public static ArrayList<saved_cart> savedcart;

    private FrameLayout fab;
    public static Dialog loaddialog2;
    private CircularTextView counter;
    private MyTextView locationtext, citytext;

    private TextView name;

    private int app_version = 1;

    public static String sel_canteen_id = "";

    public static String open_close = "1", phone = "", city = "", delivery_charges = "0", delivery_min = "0", vat_perc = "0", min_bill_amt = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        counter = (CircularTextView) findViewById(R.id.cart_counter);
        counter.setSolidColor(Color.parseColor("#ff054c"));

        locationtext = (MyTextView) findViewById(R.id.selectcity);
        citytext = (MyTextView) findViewById(R.id.selectedcity);

        Globals g = Globals.getInstance();
        g.setIp("192.168.0.11");
        g.setDirectory("foodexpress");

        savedcart = new ArrayList<>();

        cat_main = (RecyclerView) findViewById(R.id.cat_recycle);

        list = new ArrayList<>();
        adapter = new rest_list_adapter(this, list);
        cat_main.setHasFixedSize(true);
        cat_main.setNestedScrollingEnabled(false);
        cat_main.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cat_main.setAdapter(adapter);

        viewPager = (ViewPager) findViewById(R.id.flipper1);


        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselecteditem_dot));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        fab = (FrameLayout) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Double total = 0.0;

                for (int i = 0; i < MainActivity.savedcart.size(); i++) {
                    saved_cart cart = MainActivity.savedcart.get(i);

                    int quant = Integer.parseInt(cart.getQuant());
                    Double price = Double.parseDouble(cart.getPrice());

                    total += quant * price;
                    // total_quant+=quant;

                }

                if (total >= Integer.parseInt(min_bill_amt)) {
                    Intent i = new Intent(MainActivity.this, User_cart.class);

                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(MainActivity.this, "Add more items worth Rs " + String.valueOf(Integer.parseInt(min_bill_amt) - total) + " for placing order", Toast.LENGTH_LONG);

                    t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                    t.show();
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.home_hamburger);





        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout)
                        findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hader = navigationView.getHeaderView(0);

         name = (TextView) hader.findViewById(R.id.name);
        ImageView user_icon = (ImageView) hader.findViewById(R.id.user_icon);
        user_icon.setColorFilter(Color.WHITE);

        SharedPreferences sd = getSharedPreferences("user_info", MODE_PRIVATE);

        if (sd.getString("name", "").equals("")) {
            name.setText("Guest");
        } else {
            name.setText(sd.getString("name", ""));
        }


        locationtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location loc =  getLocation();

                if(loc != null)
                {
                    get_resto(loc);
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    System.out.println("address  " + address + "," + city + "," + state + "," + country + "," + postalCode + "," + knownName);

                    locationtext.setText(city + " , " + state);
                    citytext.setText(country);
                    citytext.setVisibility(View.VISIBLE);
                }
            }
        });

        getOffers();

    }

    public Location getLocation() {

        Location location = null;
        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                requestPermission();

            }else {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


                // getting GPS status
                Boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnabled) {
                    statusCheck();

                } else {

                    // getting network status
                    Boolean isNetworkEnabled = locationManager
                            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (!isGPSEnabled && !isNetworkEnabled) {
                        // no network provider is enabled
                    } else {
                        //  this.canGetLocation = true;
                        if (isNetworkEnabled) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.

                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    1000,
                                    0, this);
                            Log.d("Network", "Network Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                }
                            }
                        }
                        // if GPS Enabled get lat/long using GPS Services
                        if (isGPSEnabled) {
                            if (location == null) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        1000,
                                        0, this);
                                Log.d("GPS", "GPS Enabled");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (22 == requestCode) {
                //same request code as was in request permission
                // startService(new Intent(this, ScanBeaconService.class));
                // Toast.makeText(this, " granted", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Location loc =  getLocation();

                    if(loc != null)
                    {
                        get_resto(loc);
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        System.out.println("address  " + address + "," + city + "," + state + "," + country + "," + postalCode + "," + knownName);

                        locationtext.setText(city + " , " + state);
                        citytext.setText(country);
                    }

                }




            }

        } else {
            //not granted permission
            //show some explanation dialog that some features will not work
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, 22);
    }

    private void showExplanationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission")
                .setMessage("Allow permission to access location")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    public static void showdialog(Context context)
    {
        loaddialog2 = new Dialog(context);
       // loaddialog2.setContentView(R.layout.activity_loading_activity);
        loaddialog2.show();
    }

    public static void hidedialog()
    {
        loaddialog2.hide();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

            SharedPreferences editor = getSharedPreferences("user_info", MODE_PRIVATE);
            String name = editor.getString("name", "");

            if(!name.equals("")) {
                Intent i = new Intent(MainActivity.this, Profile.class);
                i.putExtra("from","profile");

                startActivity(i);
            }
            else
            {
                Intent i = new Intent(MainActivity.this, Signin_signup.class);
                i.putExtra("from","profile");
                startActivity(i);
            }
            // Handle the camera action
        } else {
            if (id == R.id.nav_orders) {

                Intent i = new Intent(MainActivity.this, Order_history.class);

                startActivity(i);

            } else {
                if (id == R.id.radio) {

                    Intent i = new Intent(MainActivity.this, radio_activity.class);

                    startActivity(i);

                } else if (id == R.id.nav_feedback) {

                    Intent i = new Intent(MainActivity.this, Feddback.class);

                    startActivity(i);

                } else if (id == R.id.nav_policy) {

                    Intent i = new Intent(MainActivity.this, Privacy_policy.class);

                    startActivity(i);

                } else if (id == R.id.nav_logout) {

                    SharedPreferences editor = getSharedPreferences("user_info", MODE_PRIVATE);
                    String name = editor.getString("name", "");

                    if (!name.equals("")) {

                        final Dialog d = new Dialog(MainActivity.this);

                        d.setContentView(R.layout.log_out_dialog);

                        TextView no = (TextView) d.findViewById(R.id.no_btn);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d.dismiss();
                            }
                        });

                        TextView yes = (TextView) d.findViewById(R.id.yes_btn);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                getSharedPreferences("user_info", MODE_PRIVATE).edit().clear().commit();
                                getSharedPreferences("order_details", MODE_PRIVATE).edit().clear().commit();
                                d.dismiss();
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                finish();

                            }
                        });

                        d.show();
                    }


                }
            }
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sd = getSharedPreferences("user_info", MODE_PRIVATE);

        if (sd.getString("name", "").equals("")) {
            name.setText("Guest");
        } else {
            name.setText(sd.getString("name", ""));
        }

        Location loc =  getLocation();

        if(loc != null)
        {
            get_resto(loc);
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            System.out.println("address  " + address + "," + city + "," + state + "," + country + "," + postalCode + "," + knownName);

            locationtext.setText(city + " , " + state);
            citytext.setText(country);
            citytext.setVisibility(View.VISIBLE);
        }

        if(savedcart.size()==0)
        {
            fab.setVisibility(View.GONE);
        }

        if( savedcart.size()>0)
        {
            fab.setVisibility(View.VISIBLE);
            int total_quant = 0;



            for(int i = 0; i < MainActivity.savedcart.size();i++)
            {
                saved_cart cart = MainActivity.savedcart.get(i);

                int quant = Integer.parseInt(cart.getQuant());
                //int price = Integer.parseInt(cart.getPrice());


                total_quant+=quant;

            }


            counter.setText(String.valueOf(total_quant));
        }




    }

    private void get_resto( Location loc ) {



        Globals g = Globals.getInstance();
        String url = "http://"+g.getIp()+"/"+g.getDirectory()+"/get_canteen.php";

        final LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);





        JsonObjectRequest strReq = new JsonObjectRequest(
                url,new JSONObject(), new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());


                list.clear();
                JSONArray offers = null;
                ArrayList<String> img = new ArrayList<>();

              /*  try {


                    offers = new JSONArray(response.getString("offers"));
                    for (int j = 0; j < offers.length(); j++) {

                        JSONObject js = (JSONObject) offers.get(j);

                        img.add(js.getString("image"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } */

                JSONArray restaraunts = null;


                try {


                    restaraunts = response.getJSONArray("key");
                    for (int j = 0; j < restaraunts.length(); j++) {

                        rest_main liss = new rest_main();

                        JSONObject js = (JSONObject) restaraunts.get(j);

                        liss.setRest_name(js.getString("canteen_name"));
                        liss.setRest_id(js.getString("canteen_id"));
                        liss.setRest_image(js.getString("image"));

                        liss.setRest_lati("");
                        liss.setRest_longi("");



                            liss.setRest_status(js.getString("status"));



                            liss.setRest_rating("3");


                        liss.setLocation("");
                        liss.setRest_type(js.getString("food_type"));


                        list.add(liss);


                    }

                    adapter.notifyDataSetChanged();



                    if(app_version<response.getInt("version"))
                    {
                       /* final   Dialog dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.update_dailog);

                        Button updatebutton = (Button) dialog.findViewById(R.id.updatebutton);

                        Button cancelbutton = (Button) dialog.findViewById(R.id.cancelbutton);

                        updatebutton.setOnClickListener(new View.OnClickListener()

                        {
                            @Override
                            public void onClick(View v) {
                                final String appPackageName = MainActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });

                        cancelbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show(); */

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (img.size() > 0) {

                    imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), img, img.size());
                    viewPager.setAdapter(imageFragmentPagerAdapter);
                    viewPager.setOffscreenPageLimit(img.size());
                    dotsCount = imageFragmentPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new ImageView(MainActivity.this);
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselecteditem_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(7, 0, 7, 0);

                        pager_indicator.addView(dots[i], params);
                    }

                    dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


                    // listadapter.notifyDataSetChanged();


                    //pDialog.hide();
                }

                linlaHeaderProgress.setVisibility(View.GONE);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(" ", "Error: " + error.getMessage());
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"error please try again later",Toast.LENGTH_SHORT).show();
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

    public void getOffers()
    {

        Globals g = Globals.getInstance();
        String url = "http://"+g.getIp()+"/"+g.getDirectory()+"/get_offers.php";

        final LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);




        JsonObjectRequest strReq = new JsonObjectRequest(
                url,new JSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());


                JSONArray offers = null;
                ArrayList<String> img = new ArrayList<>();

             try {


                    offers = response.getJSONArray("offers");
                    for (int j = 0; j < offers.length(); j++) {

                        JSONObject js = (JSONObject) offers.get(j);

                        img.add(js.getString("offer_image"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                 //   if(app_version<response.getInt("version"))
                  //  {
                       /* final   Dialog dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.update_dailog);

                        Button updatebutton = (Button) dialog.findViewById(R.id.updatebutton);

                        Button cancelbutton = (Button) dialog.findViewById(R.id.cancelbutton);

                        updatebutton.setOnClickListener(new View.OnClickListener()

                        {
                            @Override
                            public void onClick(View v) {
                                final String appPackageName = MainActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });

                        cancelbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show(); */

                 //   }
               // } catch (JSONException e) {
               //     e.printStackTrace();
               // }


                if (img.size() > 0) {

                    imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), img, img.size());
                    viewPager.setAdapter(imageFragmentPagerAdapter);
                    viewPager.setOffscreenPageLimit(img.size());
                    dotsCount = imageFragmentPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new ImageView(MainActivity.this);
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselecteditem_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(7, 0, 7, 0);

                        pager_indicator.addView(dots[i], params);
                    }

                    dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


                    // listadapter.notifyDataSetChanged();


                    //pDialog.hide();
                }

                linlaHeaderProgress.setVisibility(View.GONE);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(" ", "Error: " + error.getMessage());
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"error please try again later",Toast.LENGTH_SHORT).show();
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
    public void onLocationChanged(Location location) {

        System.out.println(location.toString());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}
