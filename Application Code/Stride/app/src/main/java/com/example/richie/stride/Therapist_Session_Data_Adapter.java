package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 4/5/18.
 */

public class Therapist_Session_Data_Adapter extends BaseAdapter {

    String[] meas_num;
    String[] goal;
    String[] meas_val;
    LayoutInflater mInflater;

    public Therapist_Session_Data_Adapter(Context context, String[] mn, String[] g, String[] mv ){
        meas_num = mn;
        goal = g;
        meas_val = mv;
        mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }


    @Override
    public int getCount() {
        return goal.length;
    }

    @Override
    public Object getItem(int i) {
        return goal[i];
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

        String mn = meas_num[i];
        String g = goal[i];
        String mv = meas_val[i];

        dateTextView.setText( mn );
        sessionNumTextView.setText( "" );
        goalTextView.setText( mv );
        avgTextView.setText( g );
        return v;
    }
}
