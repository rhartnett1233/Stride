package com.example.richie.stride;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DataDisplayListActivity extends AppCompatActivity {

    ListView dataDisplayList;
    String[] dataTypes;
    int measurementType;
    int curSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display_list);

        Resources res = getResources();

        dataDisplayList = (ListView) findViewById( R.id.dataDisplayList );
        dataTypes = res.getStringArray( R.array.dataDisplayTypes );

        DataDisplayListAdapter dataDisplayListAdapter = new DataDisplayListAdapter( this, dataTypes );
        dataDisplayList.setAdapter( dataDisplayListAdapter );

        Intent in = getIntent();
        measurementType = in.getIntExtra( "com.example.richie.SENSOR_INDEX", -1 );
        curSession = in.getIntExtra( "com.example.richie.SESSION_INDEX", -1);

        dataDisplayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if( i == 0 ) {
                    Intent showDataDisplay = new Intent(getApplicationContext(), SensorActivity.class);
                    showDataDisplay.putExtra("com.example.richie.ITEM_INDEX", i);
                    showDataDisplay.putExtra( "com.example.richie.SENSOR_INDEX", measurementType );
                    showDataDisplay.putExtra( "com.example.richie.SESSION_INDEX", curSession);
                    startActivity(showDataDisplay);
                }
                else if( i == 1 ){
                    Intent showGraphDisplay = new Intent(getApplicationContext(), GraphActivity.class);
                    showGraphDisplay.putExtra("com.example.richie.ITEM_INDEX", i);
                    showGraphDisplay.putExtra( "com.example.richie.SENSOR_INDEX", measurementType );
                    showGraphDisplay.putExtra( "com.example.richie.SESSION_INDEX", curSession);
                    startActivity(showGraphDisplay);
                }
            }
        });



    }
}
