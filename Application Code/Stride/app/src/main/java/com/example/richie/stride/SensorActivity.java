package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    ListView dataListView;
    String[] time;
    String[] x_axis;
    String[] y_axis;
    String[] z_axis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Context appContext = getApplicationContext();
        DataTableDO dataTableDO = new DataTableDO( appContext );

        Intent in = getIntent();
        int index = in.getIntExtra( "com.example.richie.stride.SENSOR_INDEX", -1 );

        TextView timeColTextView = (TextView) findViewById( R.id.timeColTextView );
        TextView xColTextView = (TextView) findViewById( R.id.xColTextView );
        TextView yColTextView = (TextView) findViewById( R.id.yColTextView );
        TextView zColTextView = (TextView) findViewById( R.id.zColTextView );

        timeColTextView.setText( "Timestamp" );
        xColTextView.setText( "X-Axis" );
        yColTextView.setText( "Y-Axis" );
        zColTextView.setText( "Z-Axis" );

        String[] time_0 = { "10th","11th","12th","13th"};
        String[] time_1 = { "0th","1th","2th","3th"};
        String[] time_2 = { "20th","21th","22th","23th"};

        String[] x_0 = { "-10.0","11.0","12.0","13.0" };
        String[] x_1 = { "10.0","11.0","12.0","13.0" };
        String[] x_2 = { "10.0","11.0","12.0","13.0" };

        String[] y_0 = { "10.0","-11.0","12.0","13.0" };
        String[] y_1 = { "10.0","11.0","12.0","13.0" };
        String[] y_2 = { "10.0","11.0","12.0","13.0" };

        String[] z_0 = { "10.0","11.0","12.0","-13.0" };
        String[] z_1 = { "10.0","11.0","12.0","13.0" };
        String[] z_2 = { "10.0","11.0","12.0","13.0" };

        if( index > -1 ){
            if( index == 0 ){
                //get information from Accelerometer
                time = time_0;
                x_axis = x_0;
                y_axis = y_0;
                z_axis = z_0;

            }
            else if( index == 1 ){
                //get information from Gyroscope
                time = time_0;
                x_axis = x_1;
                y_axis = y_1;
                z_axis = z_1;
            }
            else if( index == 2 ){
                //get information from Magnetometer
                time = time_0;
                x_axis = x_2;
                y_axis = y_2;
                z_axis = z_2;
            }

            dataListView = (ListView) findViewById( R.id.dataListView );
            DataAdapter dataAdapter = new DataAdapter( this, time, x_axis, y_axis, z_axis );
            dataListView.setAdapter( dataAdapter );
        }

    }

}
