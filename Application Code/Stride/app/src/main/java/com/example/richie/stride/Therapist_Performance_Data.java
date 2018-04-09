package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class Therapist_Performance_Data extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__performance__data);

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


        //////////////////////////////////////////////////////
        //getting data for patient
        final String curPatient = in.getStringExtra( "com.example.richie.PATIENT_INDEX" );
        final String data_type = in.getStringExtra( "com.example.richie.DATA_TYPE" );
        char[] data_type_list = data_type.toCharArray();
        final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO = new OverallPatientPerformanceTableDO();
        overallPatientPerformanceTableDO.setPUser( "("+curPatient+")" );
        PaginatedQueryList<OverallPatientPerformanceTableDO> allData = null;
        try {
            allData = overallPatientPerformanceTableDO.getAllUserData(dynamoDBMapper, overallPatientPerformanceTableDO);
        } catch (InterruptedException e) {
            System.out.println( "NO VALUES TO WORK WITH" );
        }

        if( allData != null ){

            //Stride or HeelToe
            if( data_type_list[0] == 'S' || data_type_list[0] == 'H' ) {
                final String[][] seperated_data;
                if( data_type_list[0] == 'S' )
                    seperated_data = overallPatientPerformanceTableDO.getStrideDataDisplay(allData);
                else
                    seperated_data = overallPatientPerformanceTableDO.getHeelToeDataDisplay(allData);

                String[] date = seperated_data[0];
                String[] session = seperated_data[1];
                String[] avg = seperated_data[2];
                String[] goal = seperated_data[3];

                /////////////////////////////////////////////
                //charting data
                lineChart = (LineChart) findViewById(R.id.lineChart);
                ArrayList<Entry> graph_avg = new ArrayList<>();
                ArrayList<Entry> graph_goal = new ArrayList<>();
                for (int i = 0; i < date.length; i++) {
                    graph_avg.add(new Entry(i, Float.parseFloat(avg[i])));
                    graph_goal.add(new Entry(i, Float.parseFloat(goal[i])));
                }

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                LineDataSet lineDataSet1 = new LineDataSet(graph_avg, "Average");
                lineDataSet1.setDrawCircles(false);
                lineDataSet1.setColor(Color.BLUE);

                LineDataSet lineDataSet2 = new LineDataSet(graph_goal, "Goal");
                lineDataSet2.setDrawCircles(false);
                lineDataSet2.setColor(Color.GREEN);

                lineDataSets.add(lineDataSet1);
                lineDataSets.add(lineDataSet2);

                lineChart.setData(new LineData(lineDataSets));
                //lineChart.setRotation(90);

                lineChart.setVisibleXRangeMaximum(65f);
                ////////////////////////////////////////////


                ////////////////////////////////////////////
                //listing values
                ListView list = (ListView) findViewById(R.id.listView);
                String[] list_session = seperated_data[1];
                String[] list_avg = seperated_data[2];
                String[] list_goal = seperated_data[3];
                for (int i = 0; i < list_session.length; i++) {
                    list_session[i] = "Session " + list_session[i];
                    list_avg[i] = "Avg: " + list_avg[i];
                    list_goal[i] = "Goal: " + list_goal[i];
                }
                Therapist_Data_Adapter adapter = new Therapist_Data_Adapter(this, seperated_data[0], list_session, list_avg, list_goal);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent nextIntent = new Intent( getApplicationContext(), Therapist_Performance_Session_Data.class );
                        char[] temp_sess = seperated_data[1][i].toCharArray();
                        String res_session = "";
                        int index_sess = 0;
                        while( index_sess < temp_sess.length ){
                            if( temp_sess[index_sess] == ' ' ){
                                index_sess++;
                                break;
                            }
                            index_sess++;
                        }
                        while( index_sess < temp_sess.length ){
                            res_session = res_session + temp_sess[index_sess];
                            index_sess++;
                        }

                        char[] temp_goal = seperated_data[3][i].toCharArray();
                        String res_goal = "";
                        int index_goal = 0;
                        while( index_goal < temp_goal.length ){
                            if( temp_goal[index_goal] == ' ' ){
                                index_goal++;
                                break;
                            }
                            index_goal++;
                        }
                        while( index_goal < temp_goal.length ){
                            res_goal = res_goal + temp_goal[index_goal];
                            index_goal++;
                        }

                        nextIntent.putExtra( "com.example.richie.CUR_PATIENT", curPatient );
                        nextIntent.putExtra( "com.example.richie.SESSION", res_session );
                        nextIntent.putExtra( "com.example.richie.DATA_TYPE", data_type );
                        nextIntent.putExtra( "com.example.richie.GOAL", res_goal );
                        startActivity( nextIntent );
                    }
                });
            }

            else if( data_type_list[0] == 'F' || data_type_list[0] == 'C' ){
                String[][] seperated_data;
                if( data_type_list[0] == 'C' )
                    seperated_data = overallPatientPerformanceTableDO.getCadenceDataDisplay(allData);
                else
                    seperated_data = overallPatientPerformanceTableDO.getFreezingDataDisplay(allData);

                String[] date = seperated_data[0];
                String[] session = seperated_data[1];
                String[] avg = seperated_data[2];
                String[] goal = seperated_data[3];

                /////////////////////////////////////////////
                //charting data
                lineChart = (LineChart) findViewById(R.id.lineChart);
                ArrayList<Entry> graph_avg = new ArrayList<>();
                ArrayList<Entry> graph_goal = new ArrayList<>();
                for (int i = 0; i < date.length; i++) {
                    graph_avg.add(new Entry(i, Float.parseFloat(avg[i])));
                    graph_goal.add(new Entry(i, Float.parseFloat(goal[i])));
                }

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                LineDataSet lineDataSet1 = new LineDataSet(graph_avg, "Average");
                lineDataSet1.setDrawCircles(false);
                lineDataSet1.setColor(Color.BLUE);

                LineDataSet lineDataSet2 = new LineDataSet(graph_goal, "Goal");
                lineDataSet2.setDrawCircles(false);
                lineDataSet2.setColor(Color.GREEN);

                lineDataSets.add(lineDataSet1);
                lineDataSets.add(lineDataSet2);

                lineChart.setData(new LineData(lineDataSets));
                //lineChart.setRotation(90);

                lineChart.setVisibleXRangeMaximum(65f);
                ////////////////////////////////////////////


                ////////////////////////////////////////////
                //listing values
                ListView list = (ListView) findViewById(R.id.listView);
                String[] list_session = seperated_data[1];
                String[] list_avg = seperated_data[2];
                String[] list_goal = seperated_data[3];
                for (int i = 0; i < list_session.length; i++) {
                    list_session[i] = "Session " + list_session[i];
                    list_avg[i] = "Avg: " + list_avg[i];
                    list_goal[i] = "Goal: " + list_goal[i];
                }
                Therapist_Data_Adapter adapter = new Therapist_Data_Adapter(this, seperated_data[0], list_session, list_avg, list_goal);
                list.setAdapter(adapter);
            }
        }

    }
}
