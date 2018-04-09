package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Therapist_Performance_Session_Data extends AppCompatActivity {

    LineChart lineChart;
    TextView textViewSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__performance__session__data);

        Intent in = getIntent();
        Context appContext = getApplicationContext();

        /**********************************************
         * Connects to AWS backend to retrieve information from database
         */
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


        //------->Need to pass session from prev page and then switch to getSessionData from patientperformanceDB<-----------//
        //////////////////////////////////////////////////////
        //getting data for patient
        final String curPatient = in.getStringExtra( "com.example.richie.CUR_PATIENT" );
        final String data_type = in.getStringExtra( "com.example.richie.DATA_TYPE" );
        final String cur_session = in.getStringExtra( "com.example.richie.SESSION" );
        final String cur_goal = in.getStringExtra( "com.example.richie.GOAL" );

        textViewSession = (TextView) findViewById( R.id.textViewSessionNumber );
        textViewSession.setText( "Session " + cur_session + ":" );

        char[] data_type_list = data_type.toCharArray();

        final PatientPerformanceDataDO patientPerformanceDataDO = new PatientPerformanceDataDO();
        patientPerformanceDataDO.setPUser( "(" + curPatient + ")" );
        PaginatedQueryList<PatientPerformanceDataDO> allData = null;
        try {
            allData = patientPerformanceDataDO.getSessionData(dynamoDBMapper, patientPerformanceDataDO, Integer.parseInt( cur_session ));
        } catch (InterruptedException e) {
            System.out.println( "NO VALUES TO WORK WITH" );
        }

        if( allData != null ){

            //Stride or HeelToe
            if( data_type_list[0] == 'S' || data_type_list[0] == 'H' ) {
                String[][] seperated_data;
                if( data_type_list[0] == 'S' )
                    seperated_data = patientPerformanceDataDO.getStrideDataDisplay(allData);
                else
                    seperated_data = patientPerformanceDataDO.getHeelToeDataDisplay(allData);

                String[] measurement = seperated_data[0];
                String[] value = seperated_data[1];

                /////////////////////////////////////////////
                //charting data
                lineChart = (LineChart) findViewById(R.id.lineChartSession);
                ArrayList<Entry> graph_value = new ArrayList<>();
                for (int i = 0; i < measurement.length; i++) {
                    graph_value.add(new Entry(i, Float.parseFloat(value[i])));
                }

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                LineDataSet lineDataSet1 = new LineDataSet(graph_value, "Value");
                lineDataSet1.setDrawCircles(false);
                lineDataSet1.setColor(Color.BLUE);

                lineDataSets.add(lineDataSet1);

                lineChart.setData(new LineData(lineDataSets));
                //lineChart.setRotation(90);

                lineChart.setVisibleXRangeMaximum(65f);
                ////////////////////////////////////////////


                ////////////////////////////////////////////
                //listing values
                ListView list = (ListView) findViewById(R.id.listViewSession);
                String[] list_meas = getMeasurementNumber(seperated_data[0]);
                String[] list_value = getFormattedValue(seperated_data[1]);
                String[] list_goal = new String[list_meas.length];
                list_goal[0] = "Goal: " + cur_goal;
                for (int i = 0; i < list_meas.length; i++) {
                    list_meas[i] = "Meas: " + list_meas[i];
                    list_value[i] = "Value: " + list_value[i];
                    list_goal[i] = "Goal: " + cur_goal;
                }
                Therapist_Session_Data_Adapter adapter = new Therapist_Session_Data_Adapter(this, list_meas, list_goal, list_value );
                list.setAdapter(adapter);
            }
        }
    }

    public String[] getMeasurementNumber( String[] array ){
        int index = 0;
        String[] meas = new String[array.length];
        while( index < array.length ){
            char[] temp_list = array[index].toCharArray();
            int inner_index = 0;
            while( true ){
                if( temp_list[inner_index] == '_' ){
                    inner_index++;
                    break;
                }
                else
                    inner_index++;
            }
            char[] xx = new char[ meas.length - inner_index];
            int temp_inner_index = 0;
            String res = "";
            while( inner_index < temp_list.length ){
                if( temp_list[inner_index] == ')' )
                    break;
                else
                    res = res + temp_list[inner_index];
                    //xx[temp_inner_index++] = temp_list[inner_index++];
                inner_index++;
                temp_inner_index++;
            }
            meas[index] = res;
            index++;
        }

        return orderMeasurements( meas );
    }


    public String[] orderMeasurements( String[] meas ){
        String[] meas_res = new String[meas.length];
        int index = 0;
        while( index < meas_res.length ){
            String temp = meas[index];
            int temp2 = Integer.parseInt( temp );
            meas_res[temp2] = meas[index];
            index++;
        }
        return meas_res;
    }


    public String[] getFormattedValue( String[] array ){
        String[] res = new String[ array.length ];
        int index = 0;
        while( index < array.length ){
            double temp = Double.parseDouble(array[index]);
            res[index] = String.format( "%.2f", temp );
            index++;
        }
        return res;
    }
}
