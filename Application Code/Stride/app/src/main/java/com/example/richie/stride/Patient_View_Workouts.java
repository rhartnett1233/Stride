package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

public class Patient_View_Workouts extends AppCompatActivity {

    private ListView workoutListView;
    private EditText editSearch;
    private ArrayAdapter<String> adapter;

    final Handler handler = new Handler();
    MQTT_Helper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__view__workouts);

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

        final String cur_patient = in.getStringExtra( "com.example.richie.CURRENT_PATIENT" );

        PatientWorkoutListDO patient = new PatientWorkoutListDO();
        patient.setPatient( cur_patient );
        PaginatedQueryList<PatientWorkoutListDO> workoutList = null;

        try {
            workoutList = patient.getWorkoutList( dynamoDBMapper, patient );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final String[] data = new String[workoutList.size()];
        final String[] time = new String[workoutList.size()];
        final String[] bpm = new String[workoutList.size()];
        final String[] type = new String[workoutList.size()];
        int i = 0;
        while( i < workoutList.size() ){
            data[i] = workoutList.get( i ).getWorkout().toString();
            bpm[i] = workoutList.get( i ).getBpm().toString();
            time[i] = workoutList.get( i ).getValue().toString();
            type[i] = workoutList.get( i ).getType().toString();
            i++;
        }


        workoutListView = (ListView) findViewById(R.id.PatientWorkoutListView);
        editSearch = (EditText) findViewById(R.id.editSearch);
        adapter = new ArrayAdapter<String>(this, R.layout.workout_list, R.id.nameTextView, data );
        workoutListView.setAdapter(adapter);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Patient_View_Workouts.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent next = new Intent( getApplicationContext(), Patient_Perform_Workout.class );
                //next.putExtra( "com.example.richie.WORKOUT_NAME", data[i] );
                next.putExtra( "com.example.richie.CUR_PATIENT", cur_patient );
                next.putExtra( "com.example.richie.WORKOUT_BPM", bpm[i] );
                next.putExtra( "com.example.richie.WORKOUT_TIME", time[i] );
                next.putExtra( "com.example.richie.WORKOUT_TYPE", type[i] );
                startActivity( next );
                finish();
            }
        });
    }



    private void startMqtt( final DynamoDBMapper dynamoDBMapper, final String cur_patient, final String workout_time, final String workout_type ) throws InterruptedException {
        OverallPatientPerformanceTableDO temp = new OverallPatientPerformanceTableDO();
        temp.setPUser( "(" + "Richie" + ")" );
        int int_sessions = temp.getTotalSessions(dynamoDBMapper, temp);
        String total_sessions = Integer.toString( int_sessions );
        System.out.println( "^^^^^^^^" );
        System.out.println( workout_type );
        System.out.println( "^^^^^^^^" );
        double goal_stride = 0;
        if( workout_type.equals("Progressive") ){
            PaginatedQueryList<OverallPatientPerformanceTableDO> xx = temp.getSessionData( dynamoDBMapper, temp, int_sessions-1);
            Map<String, String> yy = xx.get(0).getData();
            goal_stride = Double.parseDouble( yy.get("AvgStrideLength") );
            double zz = goal_stride*0.05;
            goal_stride = goal_stride + zz;
        }
        UserInfoNewDO temp_user = new UserInfoNewDO();
        temp_user.setUsername( cur_patient );
        String temp_leg_length = "";
        String temp_height = "";
        double leg_length = 0;
        double height = 0;
        PaginatedQueryList<UserInfoNewDO> list_leg = temp_user.findUser( dynamoDBMapper, temp_user );
        if( list_leg.size() > 0 ){
            temp_leg_length = list_leg.get(0).getLegLength();
            leg_length = Double.parseDouble( temp_leg_length );
            leg_length = leg_length*0.0254;

            if( workout_type.equals( "Standardized") ){
                temp_height = list_leg.get(0).getHeight();
                height = Double.parseDouble( temp_height );
                goal_stride = findStrideLength( height );
            }
        }
        final String info = workout_time + " " + total_sessions + " " + String.format( "%.2f", leg_length) + " " + cur_patient + " " + String.format( "%.2f", goal_stride);
        mqttHelper = new MQTT_Helper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "MQTT CONNECTED");
                MqttMessage myMess = new MqttMessage(info.getBytes());
                myMess.setQos(1);
                myMess.setRetained(false);
                try {
                    mqttHelper.publish("WORKOUT",myMess);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Debug", "MQTT CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.w("Debug", "MQTT DELIVERY COMPLETE!");
            }
        });
    }


    public double findStrideLength( double height ){
        double result = height*0.413*0.0254;
        return result;
    }
}
