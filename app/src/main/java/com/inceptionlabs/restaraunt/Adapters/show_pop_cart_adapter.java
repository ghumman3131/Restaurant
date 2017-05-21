package com.inceptionlabs.restaraunt.Adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inceptionlabs.restaraunt.Activities.MainActivity;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.Holders.show_pop_cart_viewholder;
import com.inceptionlabs.restaraunt.R;

import java.util.ArrayList;


/**
 * Created by ghumman on 6/22/2016.
 */
public class show_pop_cart_adapter extends RecyclerView.Adapter<show_pop_cart_viewholder>


{

    Activity context;
    ArrayList<saved_cart> list;

    public show_pop_cart_adapter(Activity context, ArrayList<saved_cart> cart)
    {
        this.context = context;
        this.list = cart;
    }

    @Override
    public show_pop_cart_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feed_user_cart_pop_up,parent,false);

        show_pop_cart_viewholder v = new show_pop_cart_viewholder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(final show_pop_cart_viewholder holder, final int position)
    {
        final saved_cart cart = list.get(position);

        holder.dish_name.setText(cart.getName());
        holder.dish_description.setText(cart.getDescription());

        final Double final_price = Double.parseDouble(cart.getPrice()) * Integer.parseInt(cart.getQuant());
        holder.price.setText(String.valueOf(final_price));
        holder.quantity.setText(cart.getQuant());

        if(cart.getVeg_non().equals("veg"))
        {
            holder.veg_non.setColorFilter(context.getResources().getColor(R.color.green));
        }

        else {
            holder.veg_non.setColorFilter(context.getResources().getColor(R.color.red));
        }

        holder.minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(cart.getQuant());

                quant--;
                if(quant==0)
                {
                    MainActivity.savedcart.remove(position);
                    notifyDataSetChanged();
                    return;

                }
                else
                {
                    Double finalprice = Double.parseDouble(cart.getPrice()) * quant;
                    holder.quantity.setText(String.valueOf(quant));
                    holder.price.setText(String.valueOf(finalprice));
                    cart.setQuant(String.valueOf(quant));
                }
            }
        });

        holder.plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(cart.getQuant());

                quant++;

                Double finalprice = Double.parseDouble(cart.getPrice()) * quant;
                holder.quantity.setText(String.valueOf(quant));
                holder.price.setText(String.valueOf(finalprice));
                cart.setQuant(String.valueOf(quant));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

