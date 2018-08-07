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

        /*UserInfoNewDO userInfoDO = new UserInfoNewDO();

        try {
            userInfoDO.createItem( dynamoDBMapper, "Sam the Wise", "stwise", "Amherst", "stwise@umass.edu", "1112223333", "Patient", "umass", "41.7", "70" );
            userInfoDO.createItem( dynamoDBMapper, "Joe Menzie", "jmenzie", "Amherst", "jMenzie@umass.edu", "1112223333", "Patient", "umass", "43.3", "73");
            userInfoDO.createItem( dynamoDBMapper, "Jack Higgins", "jhiggins", "Amherst", "jHiggins@umass.edu", "1112223333", "Patient", "umass", "41.7", "70" );
            userInfoDO.createItem( dynamoDBMapper, "Jarred Penney", "jpenney", "Amherst", "jpenney@umass.edu", "1112223333", "Patient", "umass", "41.7", "70");
            userInfoDO.createItem( dynamoDBMapper, "Richie Hartnett", "rhartnett", "Amherst", "rhartnett@umass.edu", "1112223333", "Therapist", "umass", "41.7", "70");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
}
