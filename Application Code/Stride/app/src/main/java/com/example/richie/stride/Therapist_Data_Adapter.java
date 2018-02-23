package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 2/23/18.
 */

public class Therapist_Data_Adapter extends BaseAdapter {

    String[] date;
    String[] session;
    String[] goal;
    String[] avg;
    LayoutInflater mInflater;

    public Therapist_Data_Adapter(Context context, String[] d, String[] s, String[] g, String[] a ){
        date = d;
        session = s;
        goal = g;
        avg = a;
        mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int i) {
        return date[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate( R.layout.therapist_performance_data_detail, null );
        TextView dateTextView = (TextView) v.findViewById( R.id.dateTextView );
        TextView sessionNumTextView = (TextView) v.findViewById( R.id.sessionNumTextView );
        TextView goalTextView = (TextView) v.findViewById( R.id.goalTextView );
        TextView avgTextView = (TextView) v.findViewById( R.id.avgTextView );

        String d = date[i];
        String s = session[i];
        String g = goal[i];
        String a = avg[i];

        dateTextView.setText( d );
        sessionNumTextView.setText( s );
        goalTextView.setText( g );
        avgTextView.setText( a);
        return v;
    }
}
