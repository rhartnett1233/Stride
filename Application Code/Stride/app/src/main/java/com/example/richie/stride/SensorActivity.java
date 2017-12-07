package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Map;

public class SensorActivity extends AppCompatActivity {

    ListView dataListView;
    String[] measNum;
    String[] xAxis;
    String[] yAxis;
    String[] zAxis;
    int measurement;
    int curSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Context appContext = getApplicationContext();

        /**********************************************/
        AWSConfiguration awsConfig = new AWSConfiguration(appContext);
        IdentityManager identityManager = new IdentityManager(appContext,
                awsConfig);

        IdentityManager.setDefaultIdentityManager(identityManager);
        final AWSCredentialsProvider credentialsProvider = identityManager.getCredentialsProvider();
        final String userId = identityManager.getCachedUserID();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        /**********************************************/
        final DataTableDO dataTableDO = new DataTableDO();
        dataTableDO.setUserId( "(rhartnett)" );


        Intent in = getIntent();
        measurement = in.getIntExtra( "com.example.richie.SENSOR_INDEX", -1 );
        curSession = in.getIntExtra( "com.example.richie.SESSION_INDEX", -1) + 1;

        TextView timeColTextView = (TextView) findViewById( R.id.timeColTextView );
        TextView xColTextView = (TextView) findViewById( R.id.xColTextView );
        TextView yColTextView = (TextView) findViewById( R.id.yColTextView );
        TextView zColTextView = (TextView) findViewById( R.id.zColTextView );



        if( measurement == -1 ){
            timeColTextView.setText( "ISSUE LOADING MEASUREMENTS" );
        }
        else{
            try {
                PaginatedQueryList<DataTableDO> measList = dataTableDO.getSessionData( dynamoDBMapper, dataTableDO, curSession );

                measNum = new String[ measList.size() ];
                xAxis = new String[ measList.size() ];
                yAxis = new String[ measList.size() ];
                zAxis = new String[ measList.size() ];

                if( measList != null && measList.size() != 0){
                    timeColTextView.setText( "Measurment" );
                    xColTextView.setText( "X-Axis" );
                    yColTextView.setText( "Y-Axis" );
                    zColTextView.setText( "Z-Axis" );

                    int meas_index = 0;
                    while( meas_index < measList.size() ){
                        DataTableDO curMeas = measList.get(meas_index);
                        Map curData = curMeas.getData();
                        String xval = "";
                        String yval = "";
                        String zval = "";

                        if( measurement == 0 ){
                            xval = (String) curData.get("accelerometer x");
                            yval = (String) curData.get("accelerometer y");
                            zval = (String) curData.get("accelerometer z");
                        }
                        else if( measurement == 1 ){
                            xval = (String) curData.get("gyroscope x");
                            yval = (String) curData.get("gyroscope y");
                            zval = (String) curData.get("gyroscope z");
                        }
                        else if( measurement == 2 ){
                            xval = (String) curData.get("magnetometer x");
                            yval = (String) curData.get("magnetometer y");
                            zval = (String) curData.get("magnetometerz");
                        }

                        measNum[meas_index] = Integer.toString(meas_index + 1);
                        xAxis[meas_index] = xval;
                        yAxis[meas_index] = yval;
                        zAxis[meas_index] = zval;

                        meas_index = meas_index + 1;
                        dataListView = (ListView) findViewById( R.id.dataListView );
                        DataAdapter dataAdapter = new DataAdapter( this, measNum, xAxis, yAxis, zAxis );
                        dataListView.setAdapter( dataAdapter );
                    }
                }

            } catch (InterruptedException e) {
                timeColTextView.setText( "ISSUE LOADING IN SESSION DATA" );
                System.out.println( "****************" );
                System.out.println( "CATCH" );
                System.out.println( "****************" );
                e.printStackTrace();
            }

        }

    }

}
