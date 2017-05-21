package com.inceptionlabs.restaraunt.Adapters;


import android.app.Dialog;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.inceptionlabs.restaraunt.Activities.MainActivity;
import com.inceptionlabs.restaraunt.Activities.Menu;
import com.inceptionlabs.restaraunt.AppController;
import com.inceptionlabs.restaraunt.DataModels.custom_size_price;
import com.inceptionlabs.restaraunt.DataModels.list_menu_class;
import com.inceptionlabs.restaraunt.DataModels.saved_cart;
import com.inceptionlabs.restaraunt.Fragments.Menu_fragment;
import com.inceptionlabs.restaraunt.Globals.Globals;
import com.inceptionlabs.restaraunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ghumman on 3/30/2016.
 */
public class list_menu_adapter extends RecyclerView.Adapter<list_menu_adapter.listView >
{

    private Menu_fragment context;
    private List<list_menu_class> menu_list;

    private Globals g = Globals.getInstance();
    private ImageLoader imgload = AppController.getInstance().getImageLoader();



    public list_menu_adapter(Menu_fragment context , List<list_menu_class> list ) {

        this.context = context;
        this.menu_list = list;


    }


    @Override
    public listView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_menu_list, parent, false);
        listView listview = new listView(layoutView);
        return listview;
    }

    @Override
    public void onBindViewHolder(final listView holder, final int position) {

        final list_menu_class listMenuClass = (list_menu_class) menu_list.get(position);

        holder.title.setText(listMenuClass.getName());
        holder.price.setText(listMenuClass.getPrice());
        holder.presaved.setVisibility(View.GONE);
        if(!listMenuClass.getImage().equals(""))
        {

            holder.big_image.setImageUrl("http://" + g.getIp() + "/" + g.getDirectory() + "/images/" + listMenuClass.getImage(), imgload);
            holder.big_image.setVisibility(View.VISIBLE);
        }

        if(listMenuClass.getImage().equals(""))
        {
            holder.big_image.setVisibility(View.GONE);
        }





        for(int i = 0; i < MainActivity.savedcart.size() ; i++){

            saved_cart cart = MainActivity.savedcart.get(i);
            if(cart.getItem_id().equals(listMenuClass.getItem_id())){

                holder.add_cart.setVisibility(View.GONE);

                holder.add_item_lay.setVisibility(View.VISIBLE);
                holder.quantity_text.setText(cart.getQuant());

                listMenuClass.setQuant(cart.getQuant());
                break;
            }

            else
            {
                if(!listMenuClass.getAvailability())
                {
                    holder.add_cart.setVisibility(View.GONE);
                    holder.add_item_lay.setVisibility(View.GONE);
                    holder.close_btn.setVisibility(View.GONE);
                    holder.outofstocktext.setVisibility(View.VISIBLE);
                    holder.quantity_text.setText("0");
                }
                if(listMenuClass.getAvailability())
                {
                    holder.add_cart.setVisibility(View.VISIBLE);
                    holder.add_item_lay.setVisibility(View.GONE);
                    holder.close_btn.setVisibility(View.GONE);
                    holder.outofstocktext.setVisibility(View.GONE);
                    holder.quantity_text.setText("0");
                }


            }


        }


        if(!listMenuClass.getAvailability())
        {
            holder.add_cart.setVisibility(View.GONE);
            holder.add_item_lay.setVisibility(View.GONE);
            holder.close_btn.setVisibility(View.GONE);
            holder.outofstocktext.setVisibility(View.VISIBLE);

        }
        if(listMenuClass.getAvailability())
        {

            holder.add_cart.setVisibility(View.VISIBLE);
            holder.add_item_lay.setVisibility(View.GONE);
            holder.close_btn.setVisibility(View.GONE);
            holder.outofstocktext.setVisibility(View.GONE);
        }

        if (listMenuClass.getDescription().equals(""))
        {
            holder.text_description.setVisibility(View.GONE);
        }
        if(!listMenuClass.getDescription().equals(""))
        {
            holder.text_description.setVisibility(View.VISIBLE);
            holder.text_description.setText(listMenuClass.getDescription());
        }

        if(listMenuClass.getVeg_non().equals("veg"))
        {
            holder.veg_non.setColorFilter(context.getResources().getColor(R.color.green));
        }
        else {
            holder.veg_non.setColorFilter(context.getResources().getColor(R.color.red));
        }

        if(Integer.parseInt(MainActivity.open_close)==0)
        {
            holder.add_cart.setVisibility(View.GONE);
            holder.add_item_lay.setVisibility(View.GONE);
            holder.outofstocktext.setVisibility(View.GONE);
            holder.close_btn.setVisibility(View.VISIBLE);

        }

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listMenuClass.getCustomize())

                {

                    final Dialog d = new Dialog(Menu.inst);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.getWindow().setBackgroundDrawableResource(R.drawable.cart_pop_up);

                    d.setContentView(R.layout.activity_cart);


                    TextView na = (TextView) d.findViewById(R.id.product_name);
                    na.setText(listMenuClass.getName());

                    TextView des = (TextView) d.findViewById(R.id.description);
                    des.setText(listMenuClass.getDescription());
                    LinearLayout done = (LinearLayout) d.findViewById(R.id.add_to_cart);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });

                    ListView listView = (ListView) d.findViewById(R.id.customsizelist);
                    ArrayList<custom_size_price> custom_price = new ArrayList<custom_size_price>();
                    try {
                        JSONArray jsonArray = new JSONArray(listMenuClass.getCustom_price_array());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            custom_size_price pr = new custom_size_price();

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            pr.setSize(jsonObject.getString("type"));
                            pr.setPrice(jsonObject.getString("price"));
                            pr.setItem_id(listMenuClass.getItem_id() + i);
                            pr.setName(listMenuClass.getName() + " " + jsonObject.getString("type"));
                            pr.setVeg_non(listMenuClass.getVeg_non());

                            custom_price.add(pr);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    customsizeadapter adapter = new customsizeadapter(Menu.inst, custom_price);

                    listView.setAdapter(adapter);

                    d.show();

                } else {

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



    }

    @Override
    public int getItemCount() {
        return menu_list.size();
    }

    class listView extends RecyclerView.ViewHolder {

        TextView title , price , preadded  , quantity_text , close_btn , outofstocktext , text_description;
        Button add_cart;
        LinearLayout presaved , add_item_lay , minus_image , add_image;

        ImageView veg_non  ;

        NetworkImageView  big_image;

        public listView(View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
            add_cart = (Button) itemView.findViewById(R.id.add_cart_btn);
            presaved = (LinearLayout) itemView.findViewById(R.id.showsaved);
            preadded = (TextView) itemView.findViewById(R.id.prequant);
            text_description = (TextView) itemView.findViewById(R.id.description_text);

            veg_non = (ImageView) itemView.findViewById(R.id.veg_nonveg);
            big_image = (NetworkImageView) itemView.findViewById(R.id.item_big_image);
            add_item_lay = (LinearLayout) itemView.findViewById(R.id.add_item_lay);
            minus_image = (LinearLayout) itemView.findViewById(R.id.minus_btn);
            add_image = (LinearLayout) itemView.findViewById(R.id.plus_button);
            quantity_text = (TextView) itemView.findViewById(R.id.quantity_text);
            close_btn = (TextView) itemView.findViewById(R.id.close_btn);
            outofstocktext = (TextView) itemView.findViewById(R.id.outofstocktext);

        }
    }
}

