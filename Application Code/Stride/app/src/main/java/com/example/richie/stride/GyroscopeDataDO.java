package com.example.richie.stride;

import android.content.Context;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.google.gson.Gson;
import com.amazonaws.services.dynamodbv2.model.Condition;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-GyroscopeData")

public class GyroscopeDataDO {
    private String _userId;
    private Double _xAxis;
    private Double _yAxis;
    private Double _zAxis;
    private String _timeStamp;
    private DynamoDBMapper dynamoDBMapper;
    private String userId;

    public GyroscopeDataDO( Context appContext ){
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


    /******************************************************************/
    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBAttribute(attributeName = "X-Axis")
    public Double getXAxis() {
        return _xAxis;
    }

    public void setXAxis(final Double _xAxis) {
        this._xAxis = _xAxis;
    }

    @DynamoDBAttribute(attributeName = "Y-Axis")
    public Double getYAxis() {
        return _yAxis;
    }

    public void setYAxis(final Double _yAxis) {
        this._yAxis = _yAxis;
    }

    @DynamoDBAttribute(attributeName = "Z-Axis")
    public Double getZAxis() {
        return _zAxis;
    }

    public void setZAxis(final Double _zAxis) {
        this._zAxis = _zAxis;
    }

    @DynamoDBAttribute(attributeName = "timeStamp")
    public String getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(final String _timeStamp) {
        this._timeStamp = _timeStamp;
    }
    /******************************************************************/


    /******************************************************************/
    public void createItem( GyroscopeDataDO gyroscopeDataDO_0 ) {
        final GyroscopeDataDO gyroscopeDataDO = gyroscopeDataDO_0;

        // Use IdentityManager to get the user identity id.
        gyroscopeDataDO.setUserId(this.userId);

        gyroscopeDataDO.setXAxis(10.0);
        gyroscopeDataDO.setYAxis(10.0);
        gyroscopeDataDO.setZAxis(10.0);

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(gyroscopeDataDO);

                // Item saved
            }
        }).start();
    }


    public void readItem() {
        new Thread(new Runnable() {
            @Override
            public void run() {


                GyroscopeDataDO gyroscopeDataDO = dynamoDBMapper.load(
                        GyroscopeDataDO.class,

                        // Use IdentityManager to get the user identity id.
                        userId,

                        "Article1");

                // Item read
                // Log.d("News Item:", newsItem.toString());
            }
        }).start();
    }


    public void updateItem( GyroscopeDataDO gyroscopeDataDO_0 ) {
        final GyroscopeDataDO gyroscopeDataDO = gyroscopeDataDO_0;

        // Use IdentityManager.getUserIdentityId() here to get the user identity id.
        gyroscopeDataDO.setUserId(userId);

        gyroscopeDataDO.setXAxis(10.0);
        gyroscopeDataDO.setYAxis(10.0);
        gyroscopeDataDO.setZAxis(10.0);

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(gyroscopeDataDO);

                // Item updated
            }
        }).start();
    }


    public void deleteItem(final GyroscopeDataDO gyroscopeDataDO_0 ) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                GyroscopeDataDO gyroscopeDataDO = gyroscopeDataDO_0;

                // Use IdentityManager.getUserIdentityId() here to get the user identity id.
                gyroscopeDataDO.setUserId(userId);    //partition key

                gyroscopeDataDO.setTimeStamp("Article1");  //range (sort) key

                dynamoDBMapper.delete(gyroscopeDataDO);

                // Item deleted
            }
        }).start();
    }


    public void queryNote(final GyroscopeDataDO gyroscopeDataDO_0 ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GyroscopeDataDO note = gyroscopeDataDO_0;
                note.setUserId(userId);
                note.setTimeStamp("Article1");

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("Trial"));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(note)
                        .withRangeKeyCondition("articleId", rangeKeyCondition)
                        .withConsistentRead(false);

                PaginatedList<GyroscopeDataDO> result = dynamoDBMapper.query(GyroscopeDataDO.class, queryExpression);

                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                }

                //updateOutput(stringBuilder.toString());

                if (result.isEmpty()) {
                    // There were no items matching your query.
                }
            }
        }).start();
    }

}
