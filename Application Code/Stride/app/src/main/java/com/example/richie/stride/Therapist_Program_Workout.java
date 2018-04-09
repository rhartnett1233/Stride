package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.concurrent.TimeUnit;

public class Therapist_Program_Workout extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView workoutListView;
    private Spinner spinner_bpm;
    private Spinner spinner_time;
    private Button buttonProgramWorkout;
    private ArrayAdapter<String> adapter_list;
    private ArrayAdapter<String> adapter_bpm;
    private ArrayAdapter<String> adapter_time;
    private String program_bpm = "";
    private String program_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__program__workout);

        Intent in = getIntent();
        Context appContext = getApplicationContext();

        final String curPatient = in.getStringExtra( "com.example.richie.PATIENT_INDEX" );

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

        workoutListView = (ListView) findViewById(R.id.workoutListView);
        spinner_bpm = (Spinner) findViewById( R.id.spinner_bpm );
        spinner_time = (Spinner) findViewById( R.id.spinner_time );
        buttonProgramWorkout = (Button) findViewById( R.id.buttonProgramWorkout );

        setUpListView( dynamoDBMapper, curPatient );

        //Setting up Spinners///////////////////////////////
        String[] bpm_spinner_values = { "30", "35", "40", "45", "50", "55", "60", "65", "70", "75" };
        String[] time_spinner_values = { "15", "30", "45", "60", "75", "90" };
        adapter_bpm = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, bpm_spinner_values );
        adapter_time = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, time_spinner_values );
        adapter_bpm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_bpm.setAdapter( adapter_bpm );
        spinner_time.setAdapter( adapter_time );
        spinner_bpm.setOnItemSelectedListener(this);
        spinner_time.setOnItemSelectedListener(this);
        ////////////////////////////////////////////////////

        //Setting up Button/////////////////////////////////
        buttonProgramWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( program_bpm != "" && program_time != "" ){
                    String name = program_bpm + " Steps/Min for " + program_time + "Sec";
                    PatientWorkoutListDO temp = new PatientWorkoutListDO();
                    try {
                        temp.createItem(dynamoDBMapper, curPatient, name, "time", program_time, program_bpm );
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setUpListView( dynamoDBMapper, curPatient );
                }
            }
        });
    }

    public void setUpListView( DynamoDBMapper dynamoDBMapper, String curPatient ){
        PatientWorkoutListDO patient = new PatientWorkoutListDO();
        PaginatedQueryList<PatientWorkoutListDO> workout_list = null;
        patient.setPatient( curPatient );
        try {
            workout_list = patient.getWorkoutList( dynamoDBMapper, patient );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] workout_name_list = null;
        if( workout_list != null ) {
            workout_name_list = new String[workout_list.size()];
            if(workout_list.size() == 0){
                workout_name_list = new String[1];
                workout_name_list[0] = "NO PROGRAMED WORKOUTS";
            }
            else {
                int index = 0;
                while (index < workout_list.size()) {
                    workout_name_list[index] = workout_list.get(index).getWorkout();
                    index++;
                }
            }
        }
        else {
            workout_name_list = new String[1];
            workout_name_list[0] = "NO PROGRAMED WORKOUTS";
        }

        adapter_list = new ArrayAdapter<String>(this, R.layout.workout_list, R.id.nameTextView, workout_name_list );
        workoutListView.setAdapter(adapter_list);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner temp = (Spinner) adapterView;
        if( temp.getId() == R.id.spinner_bpm ){
            program_bpm = (String) temp.getItemAtPosition( i );
        }
        else{
            program_time = (String) temp.getItemAtPosition( i );
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
