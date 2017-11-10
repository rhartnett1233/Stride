package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 11/9/17.
 */

public class SensorAdapter extends BaseAdapter {

    String[] sensors;
    LayoutInflater mInflater;

    public SensorAdapter( Context context, String[] sen ){
        sensors = sen;
        mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return sensors.length;
    }

    @Override
    public Object getItem(int i) {
        return sensors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate( R.layout.sensor_listview_detail, null );
        TextView sensorTextView = (TextView) v.findViewById( R.id.sensorTextView );

        String sen = sensors[i];

        sensorTextView.setText( sen );
        return v;
    }
}
