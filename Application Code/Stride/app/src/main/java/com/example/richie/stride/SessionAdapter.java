package com.example.richie.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Richie on 12/6/17.
 */

public class SessionAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    String[] sessions;

    public SessionAdapter(Context c, String[] s ){
        sessions = s;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sessions.length;
    }

    @Override
    public Object getItem(int i) {
        return sessions[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflator.inflate( R.layout.session_listview_detail, null );
        TextView sessionTextView = (TextView) v.findViewById( R.id.sessionTextView );

        String name = sessions[i];

        sessionTextView.setText( name );

        return v;
    }
}
