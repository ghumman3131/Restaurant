package com.inceptionlabs.restaraunt.Adapters;


import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inceptionlabs.restaraunt.Activities.MainActivity;
import com.inceptionlabs.restaraunt.Activities.Menu;
import com.inceptionlabs.restaraunt.DataModels.custom_size_price;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.R;

import java.util.ArrayList;


/**
 * Created by ghumman on 7/27/2016.
 */
public class customsizeadapter extends BaseAdapter {

    private Activity context;
    private ArrayList<custom_size_price> size_list;



    public customsizeadapter(Activity context, ArrayList<custom_size_price> size_list)
    {
        this.context = context;
        this.size_list = size_list;
    }
    @Override
    public int getCount() {
        return size_list.size();
    }

    @Override
    public Object getItem(int position) {
        return size_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        itemView = LayoutInflater.from(context).inflate(R.layout.customsizes,parent,false);

        final ViewHolder holder = new ViewHolder();

        holder.title = (TextView) itemView.findViewById(R.id.title);
        holder.price = (TextView) itemView.findViewById(R.id.item_price);
        holder.add_cart = (Button) itemView.findViewById(R.id.add_cart_btn);
        holder.presaved = (LinearLayout) itemView.findViewById(R.id.showsaved);
        holder.preadded = (TextView) itemView.findViewById(R.id.prequant);

        holder.add_item_lay = (LinearLayout) itemView.findViewById(R.id.add_item_lay);
        holder.minus_image = (LinearLayout) itemView.findViewById(R.id.minus_btn);
        holder.add_image = (LinearLayout) itemView.findViewById(R.id.plus_button);
        holder.quantity_text = (TextView) itemView.findViewById(R.id.quantity_text);

        final custom_size_price listMenuClass = size_list.get(position);


        holder.title.setText(listMenuClass.getSize());
        holder.price.setText(listMenuClass.getPrice());


        for(int i = 0; i < MainActivity.savedcart.size() ; i++) {

            saved_cart cart = MainActivity.savedcart.get(i);
            if (cart.getItem_id().equals(listMenuClass.getItem_id())) {

                holder.add_cart.setVisibility(View.GONE);

                holder.add_item_lay.setVisibility(View.VISIBLE);
                holder.quantity_text.setText(cart.getQuant());

                listMenuClass.setQuant(cart.getQuant());
                break;
            }
            else {
                holder.add_cart.setVisibility(View.VISIBLE);

                holder.add_item_lay.setVisibility(View.GONE);
                holder.quantity_text.setText("1");


            }
        }

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.add_cart.setVisibility(View.GONE);
                holder.add_item_lay.setVisibility(View.VISIBLE);
                holder.quantity_text.setText("1");

                for (int i = 0; i < MainActivity.savedcart.size(); i++) {
                    saved_cart cart = MainActivity.savedcart.get(i);
                    if (cart.getItem_id().equals(listMenuClass.getItem_id())) {


                        MainActivity.savedcart.remove(i);
                    }
                }

                saved_cart savedcart = new saved_cart();

                savedcart.setItem_id(listMenuClass.getItem_id());
                savedcart.setName(listMenuClass.getName());
                savedcart.setQuant(holder.quantity_text.getText().toString());
                savedcart.setDescription("");
                savedcart.setPrice(listMenuClass.getPrice());
                savedcart.setVeg_non(listMenuClass.getVeg_non());

                MainActivity.savedcart.add(savedcart);

                final Toast toast = Toast.makeText(Menu.inst, "item added", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.RIGHT, 30, 50);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 800);

                Menu.updatefab();
            }




        });


        holder.add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(holder.quantity_text.getText().toString());

                quant++;



                holder.quantity_text.setText(String.valueOf(quant));

                for(int i = 0 ; i <MainActivity.savedcart.size(); i++)
                {
                    saved_cart cart = MainActivity.savedcart.get(i);
                    if(cart.getItem_id().equals(listMenuClass.getItem_id()))
                    {


                        MainActivity.savedcart.remove(i);
                    }
                }

                saved_cart savedcart = new saved_cart();

                savedcart.setItem_id(listMenuClass.getItem_id());
                savedcart.setName(listMenuClass.getName());
                savedcart.setQuant(holder.quantity_text.getText().toString());
                savedcart.setDescription("");
                savedcart.setPrice(listMenuClass.getPrice());
                savedcart.setVeg_non(listMenuClass.getVeg_non());

                MainActivity.savedcart.add(savedcart);
                final Toast toast = Toast.makeText(Menu.inst, "item added", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.RIGHT, 30, 50);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 800);

                Menu.updatefab();
            }
        });

        holder.minus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(holder.quantity_text.getText().toString()) == 0)

                {


                } else {

                    int quant = Integer.parseInt(holder.quantity_text.getText().toString());

                    quant--;

                    if (quant == 0) {
                        holder.add_item_lay.setVisibility(View.GONE);
                        holder.quantity_text.setText(String.valueOf(quant));
                        holder.add_cart.setVisibility(View.VISIBLE);

                        for (int i = 0; i < MainActivity.savedcart.size(); i++) {
                            saved_cart cart = MainActivity.savedcart.get(i);
                            if (cart.getItem_id().equals(listMenuClass.getItem_id())) {


                                MainActivity.savedcart.remove(i);
                            }
                        }
                        Menu.updatefab();
                        final Toast toast = Toast.makeText(Menu.inst, "item removed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 30, 50);
                        toast.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 800);
                        return;
                    }


                    holder.quantity_text.setText(String.valueOf(quant));


                }

                for (int i = 0; i < MainActivity.savedcart.size(); i++) {
                    saved_cart cart = MainActivity.savedcart.get(i);
                    if (cart.getItem_id().equals(listMenuClass.getItem_id())) {


                        MainActivity.savedcart.remove(i);
                    }
                }

                saved_cart savedcart = new saved_cart();

                savedcart.setItem_id(listMenuClass.getItem_id());
                savedcart.setName(listMenuClass.getName());
                savedcart.setQuant(holder.quantity_text.getText().toString());
                savedcart.setDescription("");
                savedcart.setPrice(listMenuClass.getPrice());
                savedcart.setVeg_non(listMenuClass.getVeg_non());

                MainActivity.savedcart.add(savedcart);
                final Toast toast = Toast.makeText(Menu.inst, "item removed", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.RIGHT, 30, 50);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 800);
                Menu.updatefab();
            }


        });




        return itemView;
    }

    private class ViewHolder{
        TextView title , price , preadded  , quantity_text ;
        Button add_cart;
        LinearLayout presaved , add_item_lay , minus_image , add_image;




    }
}

