package com.inceptionlabs.restaraunt.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.inceptionlabs.restaraunt.R;

/**
 * Created by ghumman on 11/12/2016.
 */

public class cat_main_view_holder extends RecyclerView.ViewHolder {

    public TextView rest_name , rating , type , directions , status;

    public ImageView rest_image;
    public LinearLayout rest_lay;

    public cat_main_view_holder(View itemView) {
        super(itemView);

        this.rest_name = (TextView) itemView.findViewById(R.id.cat_title);
        this.rest_image = (ImageView) itemView.findViewById(R.id.rest_image);
        this.rest_lay = (LinearLayout) itemView.findViewById(R.id.cat_lay);
        this.rating = (TextView)  itemView.findViewById(R.id.ratingtext);
        this.type = (TextView) itemView.findViewById(R.id.resttype);
        this.directions = (TextView) itemView.findViewById(R.id.directionstext);
        this.status = (TextView) itemView.findViewById(R.id.reststatus);
    }
}
