package com.inceptionlabs.restaraunt.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inceptionlabs.restaraunt.Custom.MyTextView;
import com.inceptionlabs.restaraunt.R;

/**
 * Created by ghumman on 5/21/2017.
 */

public class order_history_holder extends RecyclerView.ViewHolder {

    public MyTextView orderid , bill , view_items ;

    public order_history_holder(View itemView) {
        super(itemView);

        orderid = (MyTextView) itemView.findViewById(R.id.orderid);

        bill = (MyTextView) itemView.findViewById(R.id.bill);

        view_items = (MyTextView) itemView.findViewById(R.id.view_items);
    }
}
