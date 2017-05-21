package com.inceptionlabs.restaraunt.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.inceptionlabs.restaraunt.Activities.MainActivity;
import com.inceptionlabs.restaraunt.Activities.Menu;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.DataModels.rest_main;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.Holders.cat_main_view_holder;
import com.inceptionlabs.restaraunt.R;

import java.util.ArrayList;

/**
 * Created by ghumman on 6/12/2016.
 */
public class rest_list_adapter extends RecyclerView.Adapter<cat_main_view_holder> {

    ArrayList<rest_main> list;
    Activity context;
    ImageLoader imgloader = AppController.getInstance().getImageLoader();
    Globals g = Globals.getInstance();

    public rest_list_adapter(Activity context , ArrayList<rest_main> list) {

        this.context = context;
        this.list = list;
    }
    @Override
    public cat_main_view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rest_grid,parent,false);
        return new cat_main_view_holder(v);
    }

    @Override
    public void onBindViewHolder(cat_main_view_holder holder, final int position) {

        final rest_main name = list.get(position);

        holder.rest_name.setText(name.getRest_name());
        holder.type.setText(name.getRest_type());
        holder.directions.setText(name.getLocation());
        holder.status.setText(name.getRest_status());
        holder.rating.setText(name.getRest_rating());
        if(Double.parseDouble(name.getRest_rating())>=4)
        {
            holder.rating.setBackground(context.getResources().getDrawable(R.drawable.green_round_rect));
        }
        else if(Double.parseDouble(name.getRest_rating())>=3.5)
        {
            holder.rating.setBackground(context.getResources().getDrawable(R.drawable.light_green_rect));
        }
        else {
            holder.rating.setBackground(context.getResources().getDrawable(R.drawable.yellow_rect));

        }

        if(name.getRest_status().equals("OPEN"))
        {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));

        }



       // holder.cat_image.setImageUrl("http://" + g.getIp() + "/" + g.getDirectory() + "/images/" + name.getRest_image(), imgloader);

        holder.rest_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // MainActivity.showdialog(context);

                if(name.getRest_status().equals("CLOSE"))
                {
                    Toast.makeText(context,"CLOSED NOW",Toast.LENGTH_SHORT).show();
                }

                else {
                    Activity activity = (Activity) context;
                    Intent i = new Intent(context, Menu.class);

                    MainActivity.sel_canteen_id = name.getRest_id();
                    i.putExtra("rest_id", name.getRest_id());
                    activity.startActivity(i);

                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

