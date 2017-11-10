package com.example.richie.stride;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        Context appContext = getApplicationContext();

        AccelerometerDataDO accelerometerDataDO = new AccelerometerDataDO( appContext );
        GyroscopeDataDO gyroscopeDataDO = new GyroscopeDataDO( appContext );
        MagnometerDataDO magnometerDataDO = new MagnometerDataDO( appContext );

        SensorAdapter sensorAdapter = new SensorAdapter( this, sensors );
        sensorListView.setAdapter( sensorAdapter );

    }

}
