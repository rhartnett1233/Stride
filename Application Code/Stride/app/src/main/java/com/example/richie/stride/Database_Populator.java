package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Database_Populator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database__populator);

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

        PatientWorkoutListDO patientWorkoutListDO = new PatientWorkoutListDO();

        patientWorkoutListDO.setPatient( "Richie" );
        patientWorkoutListDO.setType( "time" );
        patientWorkoutListDO.setValue( "30" );
        patientWorkoutListDO.setBpm( "60" );
        patientWorkoutListDO.setWorkout( "60 steps/min for 30 sec" );

        try {
            patientWorkoutListDO.createItem( dynamoDBMapper, patientWorkoutListDO );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        PatientWorkoutListDO patientWorkoutListDO1 = new PatientWorkoutListDO();

        patientWorkoutListDO1.setPatient( "Richie" );
        patientWorkoutListDO1.setType( "time" );
        patientWorkoutListDO1.setValue( "60" );
        patientWorkoutListDO1.setBpm( "40" );
        patientWorkoutListDO1.setWorkout( "40 steps/min for 30 sec" );

        try {
            patientWorkoutListDO1.createItem( dynamoDBMapper, patientWorkoutListDO1 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
