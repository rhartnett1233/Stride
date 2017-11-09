package com.example.richie.stride;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class MainActivity extends AppCompatActivity {
    DynamoDBMapper dynamoDBMapper;

    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context appContext = getApplicationContext();

        AccelerometerDataDO accelerometerDataDO = new AccelerometerDataDO( appContext );
        GyroscopeDataDO gyroscopeDataDO = new GyroscopeDataDO( appContext );
        MagnometerDataDO magnometerDataDO = new MagnometerDataDO( appContext );

    }

}
