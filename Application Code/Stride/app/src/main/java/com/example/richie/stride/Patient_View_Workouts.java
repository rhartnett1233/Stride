package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Patient_View_Workouts extends AppCompatActivity {

    private ListView workoutListView;
    private EditText editSearch;
    private ArrayAdapter<String> adapter;

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
        int i = 0;
        while( i < workoutList.size() ){
            data[i] = workoutList.get( i ).getWorkout().toString();
            bpm[i] = workoutList.get( i ).getBpm().toString();
            time[i] = workoutList.get( i ).getValue().toString();
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
                startActivity( next );
                finish();
            }
        });
    }
}
