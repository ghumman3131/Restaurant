package com.inceptionlabs.restaraunt.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.inceptionlabs.restaraunt.Activities.Menu;
import com.inceptionlabs.restaraunt.Adapters.list_menu_adapter;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.DataModels.list_menu_class;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ghumman on 3/26/2016.
 */
public class Menu_fragment extends Fragment {

    String selected_menu;
    private List<list_menu_class> menu_list;
    RecyclerView recyclerview;

    list_menu_adapter listadapter;
    ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_fragment, container, false);

        selected_menu = getArguments().getString("extra");
        menu_list = new ArrayList<list_menu_class>();

        recyclerview = (RecyclerView) rootview.findViewById(R.id.item_list);

        Menu_fragment inst = this;

        listadapter = new list_menu_adapter(inst,menu_list );
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerview.setItemAnimator(itemAnimator);
        recyclerview.setAdapter(listadapter);
        pb = (ProgressBar) rootview.findViewById(R.id.pbHeaderProgress);
        pb.setVisibility(View.VISIBLE);

        // MainActivity.showdialog(getActivity());
        getmenu();


        return rootview;
    }

    private void getmenu()
    {



        Globals g = Globals.getInstance();

        String  tag_string_req = "string_req";

        String url = "http://"+g.getIp()+"/"+g.getDirectory()+"/get_full_menu2.php";




        JSONObject Parent = new JSONObject();

        try {
            Parent.put("selected_item",selected_menu);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(
                url,Parent,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                JSONArray respo = null;

                try {
                    respo = response.getJSONArray("products");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i<respo.length();i++)
                {
                    try {

                        JSONObject jsonobject = (JSONObject) respo.get(i);
                        list_menu_class list = new list_menu_class();
                        list.setName(jsonobject.getString("name"));

                        list.setVeg_non(jsonobject.getString("veg_non"));

                        list.setImage("");
                        list.setDescription("");
                        list.setQuant("0");
                        list.setItem_id(jsonobject.getString("id"));


                        int cust = (0);
                        int available = (1);

                        if(cust == 1){
                            list.setCustomize(true);
                            JSONArray price_array = new JSONArray(jsonobject.getString("price"));
                            JSONObject jsonObject1 = (JSONObject) price_array.get(0);
                            list.setPrice(jsonObject1.getString("price"));
                            list.setCustom_price_array(price_array.toString());
                        }
                        else {
                            list.setCustomize(false);
                            list.setPrice(jsonobject.getString("price"));
                        }
                        if(available ==1)
                        {
                            list.setAvailability(true);
                        }
                        else {
                            list.setAvailability(false);
                        }

                        menu_list.add(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listadapter.notifyDataSetChanged();

                }
                //  MainActivity.hidedialog();
                pb.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(" ", "Error: " + error.getMessage());
                // MainActivity.hidedialog();
                pb.setVisibility(View.GONE);
            }
        });


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200) {

            if (resultCode== Activity.RESULT_OK){
                int position = data.getIntExtra("position",0);
                listadapter.notifyItemChanged(position);
                System.out.println("dooooo");

                Menu.updatefab();
            }
        }
    }
}


