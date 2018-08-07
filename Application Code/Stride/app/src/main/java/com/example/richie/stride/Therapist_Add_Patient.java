package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Therapist_Add_Patient extends AppCompatActivity {

    private EditText entered_user;
    private Button btn_add;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__add__patient);

        Context appContext = getApplicationContext();
        Intent in = getIntent();

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


        final String cur_therapist = in.getStringExtra( "com.example.richie.CURRENT_THERAPIST" );

        entered_user = (EditText) findViewById( R.id.editTextUser );
        btn_add = (Button) findViewById( R.id.buttonAddPat );
        error = (TextView) findViewById( R.id.textViewError );

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_user = entered_user.getText().toString();
                UserInfoNewDO userInfoDO = new UserInfoNewDO();
                userInfoDO.setUsername( temp_user );
                System.out.println( "^^^^^^^^^^" );
                System.out.println( temp_user );
                System.out.println( "^^^^^^^^^^" );
                PaginatedQueryList<UserInfoNewDO> user_found = null;
                try {
                    user_found = userInfoDO.findUser( dynamoDBMapper, userInfoDO );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if( user_found.size() == 1 ){
                    userInfoDO = user_found.get(0);
                    if(userInfoDO.getUserType().equals("Patient") || userInfoDO.getUserType().equals("patient") ){
                        TherapistPatientListTableDO therapistPatientListTableDO = new TherapistPatientListTableDO();
                        try {
                            therapistPatientListTableDO.createItem( dynamoDBMapper, cur_therapist, temp_user );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent next = new Intent( Therapist_Add_Patient.this, Therapist_Second_Screen.class );
                        next.putExtra( "com.example.richie.PATIENT_INDEX", temp_user );
                        next.putExtra( "com.example.richie.CUR_THERAPIST", cur_therapist );
                        startActivity( next );
                    }
                    else{
                        error.setText( "Error: Patient Does Not Exist");
                    }
                }
                else{
                    error.setText( "Error: Patient Does Not Exist");
                }
            }
        });


    }
}
