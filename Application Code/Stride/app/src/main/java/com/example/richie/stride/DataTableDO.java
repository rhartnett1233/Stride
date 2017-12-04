package com.example.richie.stride;

import android.content.Context;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;

import java.util.Map;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-DataTable")

public class DataTableDO {
    private String _userId;
    private String _sessionID;
    private Map<String, String> _data;
    private String userId;
    private DynamoDBMapper dynamoDBMapper;

    public DataTableDO( Context appContext ){
        AWSConfiguration awsConfig = new AWSConfiguration(appContext);
        IdentityManager identityManager = new IdentityManager(appContext,
                awsConfig);
        IdentityManager.setDefaultIdentityManager(identityManager);
        final AWSCredentialsProvider credentialsProvider = identityManager.getCredentialsProvider();
        userId = identityManager.getCachedUserID();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();

        _userId = userId;
    }

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBRangeKey(attributeName = "sessionID")
    @DynamoDBAttribute(attributeName = "sessionID")
    public String getSessionID() {
        return _sessionID;
    }

    public void setSessionID(final String _sessionID) {
        this._sessionID = _sessionID;
    }

    @DynamoDBAttribute(attributeName = "Data")
    public Map<String, String> getData() {
        return _data;
    }

    public void setData(final Map<String, String> _data) {
        this._data = _data;
    }

    public String getUserName(Context appContext, AWSConfiguration awsConfig ){
        CognitoUserPool userPool = new CognitoUserPool( appContext, awsConfig );
        CognitoUser user = userPool.getCurrentUser();
        return user.getUserId();
    }
    /******************************************************************/


    /******************************************************************/
    public void createItem( DataTableDO dataTableDO_0 ) {
        final DataTableDO dataTableDO = dataTableDO_0;

        // Use IdentityManager to get the user identity id.
        dataTableDO.setUserId(this.userId);

        dataTableDO.setUserId( dataTableDO.getUserId() );
        dataTableDO.setSessionID( dataTableDO.getSessionID() );
        dataTableDO.setData(dataTableDO.getData());

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(dataTableDO);

                // Item saved
            }
        }).start();
    }


    public void readItem( DataTableDO dataTableDO ) {
        final String sortKey = dataTableDO.getSessionID();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataTableDO dataTableDO = dynamoDBMapper.load(
                        DataTableDO.class,

                        // Use IdentityManager to get the user identity id.
                        userId,

                        sortKey);

                // Item read
                // Log.d("News Item:", newsItem.toString());
            }
        }).start();
    }


    public void updateItem( DataTableDO dataTableDO_0 ) {
        final DataTableDO dataTableDO = dataTableDO_0;

        // Use IdentityManager.getUserIdentityId() here to get the user identity id.
        dataTableDO.setUserId(userId);

        dataTableDO.setSessionID( dataTableDO.getSessionID() );
        dataTableDO.setData( dataTableDO.getData() );

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(dataTableDO);

                // Item updated
            }
        }).start();
    }


    public void deleteItem(final DataTableDO dataTableDO_0 ) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                DataTableDO dataTableDO = dataTableDO_0;

                // Use IdentityManager.getUserIdentityId() here to get the user identity id.
                dataTableDO.setUserId(userId);    //partition key

                dataTableDO.setSessionID( dataTableDO.getSessionID() );  //range (sort) key

                dynamoDBMapper.delete(dataTableDO);

                // Item deleted
            }
        }).start();
    }


    public void queryNote(final DataTableDO dataTableDO_0 ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataTableDO note = dataTableDO_0;
                note.setUserId(userId);
                note.setSessionID( note.getSessionID() );

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("Trial"));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(note)
                        .withRangeKeyCondition("articleId", rangeKeyCondition)
                        .withConsistentRead(false);

                PaginatedList<DataTableDO> result = dynamoDBMapper.query(DataTableDO.class, queryExpression);

                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                }

                //updateOutput(stringBuilder.toString());

                if (result.isEmpty()) {
                    System.out.println( "no items matching query" );
                    // There were no items matching your query.
                }
            }
        }).start();
    }

}
