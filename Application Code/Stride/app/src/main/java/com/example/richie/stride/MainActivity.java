package com.example.richie.stride;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView sensorListView;
    String[] sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        sensorListView = (ListView) findViewById( R.id.sensorListView );
        sensors = res.getStringArray( R.array.sensors );

        SensorAdapter sensorAdapter = new SensorAdapter( this, sensors );
        sensorListView.setAdapter( sensorAdapter );



        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showSensorActivity = new Intent( getApplicationContext(), SensorActivity.class );
                showSensorActivity.putExtra( "com.example.richie.stride.SENSOR_INDEX", i );
                startActivity( showSensorActivity );
            }
        });

    }

}
