package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {

    LineChart lineChart;
    int measurement;
    int curSession;
    ArrayList<Entry> xAxes;
    ArrayList<Entry> yAxes;
    ArrayList<Entry> zAxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

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


        lineChart = (LineChart) findViewById( R.id.lineChart );
        xAxes = new ArrayList<>();
        yAxes = new ArrayList<>();
        zAxes = new ArrayList<>();

        if( measurement == -1 ){
            System.out.println( "ERROR LOADING INFORMATION FROM MEASUREMENT" );
        }
        else {
            try {
                PaginatedQueryList<DataTableDO> measList = dataTableDO.getSessionData(dynamoDBMapper, dataTableDO, curSession);

                if( measList != null && measList.size() != 0){
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

                        xAxes.add( new Entry(meas_index, Float.parseFloat(xval)) );
                        yAxes.add( new Entry(meas_index, Float.parseFloat(yval)) );
                        zAxes.add( new Entry(meas_index, Float.parseFloat(zval)) );

                        meas_index = meas_index + 1;
                    }

                    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                    LineDataSet lineDataSet1 = new LineDataSet( xAxes, "X_Direction" );
                    lineDataSet1.setDrawCircles( false );
                    lineDataSet1.setColor( Color.BLUE );

                    LineDataSet lineDataSet2 = new LineDataSet( yAxes, "Y_Direction" );
                    lineDataSet2.setDrawCircles( false );
                    lineDataSet2.setColor( Color.RED );

                    LineDataSet lineDataSet3 = new LineDataSet( zAxes, "Z_Direction" );
                    lineDataSet3.setDrawCircles( false );
                    lineDataSet3.setColor( Color.GREEN );

                    lineDataSets.add( lineDataSet1 );
                    lineDataSets.add( lineDataSet2 );
                    lineDataSets.add( lineDataSet3 );

                    lineChart.setData( new LineData(lineDataSets) );

                    lineChart.setVisibleXRangeMaximum(65f);
                }

            } catch (InterruptedException e) {
                System.out.println("ERROR LOADING IN SESSION DATA");
                System.out.println("****************");
                System.out.println("CATCH");
                System.out.println("****************");
                e.printStackTrace();
            }
        }

        /*double x = 0;
        int numDataPoints = 1000;

        for( int i = 0; i < numDataPoints; i++ ){
            float sinFunction = Float.parseFloat( String.valueOf(Math.sin(x)) );
            float cosFunction = Float.parseFloat( String.valueOf(Math.cos(x)) );
            x = x + 0.1;
            yAxesSin.add( new Entry(sinFunction, i) );
            yAxesCos.add( new Entry(cosFunction, i) );
            xAxes.add( i, String.valueOf(x) );
        }

        String[] xAxesString = new String[xAxes.size()];
        for( int i = 0; i < xAxes.size(); i++ ){
            xAxesString[i] = xAxes.get(i).toString();
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet( yAxesCos, "cos" );
        lineDataSet1.setDrawCircles( false );
        lineDataSet1.setColor( Color.BLUE );

        LineDataSet lineDataSet2 = new LineDataSet( yAxesSin, "sin" );
        lineDataSet2.setDrawCircles( false );
        lineDataSet2.setColor( Color.RED );

        lineDataSets.add( lineDataSet1 );
        lineDataSets.add( lineDataSet2 );

        lineChart.setData( new LineData(lineDataSets) );

        lineChart.setVisibleXRangeMaximum(65f);*/
    }
}
