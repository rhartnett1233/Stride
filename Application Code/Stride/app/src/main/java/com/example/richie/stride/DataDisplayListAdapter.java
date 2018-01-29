package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 1/28/18.
 */

public class DataDisplayListAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    String[] dataTypes;

    public DataDisplayListAdapter(Context c, String[] types ){
        dataTypes = types;
        mInflator = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return dataTypes.length;
    }

    @Override
    public Object getItem(int i) {
        return dataTypes[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflator.inflate( R.layout.data_display_list_detail, null );
        TextView dataTypeTextView = (TextView) v.findViewById( R.id.dataTypeTextView );

        String name = dataTypes[i];

        dataTypeTextView.setText(name);
        return v;
    }
}
