package com.inceptionlabs.restaraunt.Adapters;





import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inceptionlabs.restaraunt.DataModels.rest_main;
import com.inceptionlabs.restaraunt.R;


// TODO: Auto-generated Javadoc
/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class LeftNavAdapter extends BaseAdapter

{

    /** The items. */
    private List<rest_main> items;

    /** The context. */
    private Context context;

    /**
     * Instantiates a new left nav adapter.
     *  @param context the context
     * @param items the items
     */
    public LeftNavAdapter(Context context, List<rest_main> items)
    {
        this.context = context;
        this.items = items;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        return items.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public rest_main getItem(int arg0)
    {
        return items.get(arg0);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.left_nav_item, null);
        TextView lbl = (TextView) convertView.findViewById(R.id.l);
        ImageView selected = (ImageView) convertView.findViewById(R.id.selected_category_tick);
        final rest_main info = items.get(position);
        lbl.setText(info.getRest_name());
        if(info.getSelcted())
        {
            // lbl.setCompoundDrawablesWithIntrinsicBounds(info.getImageSelected(), 0, 0, 0);

            selected.setColorFilter(context.getResources().getColor(R.color.green));


        }
        else
        {
            // lbl.setCompoundDrawablesWithIntrinsicBounds(info.getImage(), 0, 0, 0);
            selected.setColorFilter(context.getResources().getColor(R.color.light_grey));
        }
        return convertView;
    }

}
