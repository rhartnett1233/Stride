package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 11/10/17.
 */

public class DataAdapter extends BaseAdapter {

    String[] x_axis;
    String[] y_axis;
    String[] z_axis;
    String[] time;
    LayoutInflater mInflater;

    public DataAdapter( Context context, String[] t, String[] x, String[] y, String[] z ){
        time = t;
        x_axis = x;
        y_axis = y;
        z_axis = z;
        mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return time.length;
    }

    @Override
    public Object getItem(int i) {
        return time[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate( R.layout.data_listview_detail, null );
        TextView timeTextView = (TextView) v.findViewById( R.id.timeTextView );
        TextView xTextView = (TextView) v.findViewById( R.id.xTextView );
        TextView yTextView = (TextView) v.findViewById( R.id.yTextView );
        TextView zTextView = (TextView) v.findViewById( R.id.zTextView );

        String t = time[i];
        String x = x_axis[i];
        String y = y_axis[i];
        String z = z_axis[i];

        timeTextView.setText( t );
        xTextView.setText( x );
        yTextView.setText( y );
        zTextView.setText( z );
        return v;
    }
}


