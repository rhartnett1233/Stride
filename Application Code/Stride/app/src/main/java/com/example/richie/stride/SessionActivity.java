package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class SessionActivity extends AppCompatActivity {

    ListView sessionListView;
    String[] sessions;
    int totalSessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

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
        Context con = getApplicationContext();
        //dataTableDO.findUserName( con, awsConfig );
        dataTableDO.setUserId( "(rhartnett)" );

        try {
            totalSessions = dataTableDO.getTotalSessions( dynamoDBMapper, dataTableDO );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sessions = new String[totalSessions];
        int index = 1;
        while( index <= totalSessions ){
            String tempNum = Integer.toString( index );
            String tempSess = "Session " + tempNum;
            sessions[index-1] = tempSess;
            index = index + 1;
        }


        Resources res = getResources();
        sessionListView = (ListView) findViewById( R.id.sessionListView );

        SessionAdapter sessionAdapter = new SessionAdapter( this, sessions );
        sessionListView.setAdapter( sessionAdapter );

        sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showSensorListView = new Intent( getApplicationContext(), MeasurementActivity.class );
                showSensorListView.putExtra( "com.example.richie.SESSION_INDEX", i );
                startActivity( showSensorListView );
            }
        });



    }
}
