package com.inceptionlabs.restaraunt.Holders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inceptionlabs.restaraunt.R;


/**
 * Created by ghumman on 6/22/2016.
 */
public class show_pop_cart_viewholder extends RecyclerView.ViewHolder {

    public  TextView dish_name , dish_description , price , quantity;

    public ImageView veg_non , minus_button , plus_button;

    public show_pop_cart_viewholder(View itemView) {
        super(itemView);

        dish_name = (TextView) itemView.findViewById(R.id.dish_name);
        dish_description = (TextView) itemView.findViewById(R.id.dish_description);
        price = (TextView) itemView.findViewById(R.id.dish_price);
        quantity = (TextView) itemView.findViewById(R.id.quantity_text);

        veg_non = (ImageView) itemView.findViewById(R.id.veg_nonveg);
        minus_button = (ImageView) itemView.findViewById(R.id.minus_btn);
        plus_button = (ImageView) itemView.findViewById(R.id.plus_button);
    }
}

