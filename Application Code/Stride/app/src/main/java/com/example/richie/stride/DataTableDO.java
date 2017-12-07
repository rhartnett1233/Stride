package com.example.richie.stride;

import android.content.Context;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.Map;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-DataTable")

public class DataTableDO {
    private String _userId;
    private String _sessionID;
    private Map<String, String> _data;
    private String userName;
    private PaginatedQueryList<DataTableDO> sessionData;
    private int totalSessions;


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

    public String getUserName(){ return userName; }

    public String findUserName(Context appContext, AWSConfiguration awsConfig ){
        CognitoUserPool userPool = new CognitoUserPool( appContext, awsConfig );
        CognitoUser user = userPool.getCurrentUser();
        userName = user.getUserId();
        return userName;
    }
    /******************************************************************/


    /******************************************************************/
    public void display(){
        System.out.println("*********************************************");
        System.out.println( "UserName: " + userName );
        System.out.println( "SessionID: " + getSessionID() );
        System.out.println( "Map: " + getData() );
        System.out.println("*********************************************");
    }


    public void createItem( final DynamoDBMapper dynamoDBMapper, String user, String session, Map<String, String> data ) throws InterruptedException {
        final DataTableDO dataTableDO = new DataTableDO();
        dataTableDO.setUserId( user );
        dataTableDO.setSessionID( session );
        dataTableDO.setData( data );
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.save( dataTableDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void readItem( final DynamoDBMapper dynamoDBMapper, final String hashKey, final String rangeKey ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                DataTableDO dd = dynamoDBMapper.load( DataTableDO.class, hashKey, rangeKey );
                if( dd == null )
                    System.out.println( "DID NOT WORK !!!!!!!!!!!!!" );
                else {
                    System.out.println("*** DATA *** ");
                    System.out.println( dd.getData() );
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void updateItem( final DynamoDBMapper dynamoDBMapper, final DataTableDO dataTableDO, final Map<String, String> data ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                DataTableDO dd = dynamoDBMapper.load( DataTableDO.class, "("+dataTableDO.getUserName()+")" , "("+dataTableDO.getSessionID()+")" );
                if( dd == null )
                    System.out.println( "DID NOT WORK !!!!!!!!!!!!!" );
                else {
                    dd.setData( data );
                    dynamoDBMapper.save( dd );
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void deleteItem(final DynamoDBMapper dynamoDBMapper, final DataTableDO dataTableDO ) {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.delete( dataTableDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public PaginatedQueryList<DataTableDO> getSessionData(final DynamoDBMapper dynamoDBMapper, final DataTableDO dataTableDO, final int session ) throws InterruptedException {
        String temp = Integer.toString( session );
        final String sess = "(" + temp + "_";
        Runnable runnable = new Runnable() {
            public void run() {
                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(sess.toString()));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(dataTableDO)
                        .withRangeKeyCondition("sessionID", rangeKeyCondition)
                        .withConsistentRead(false);

                sessionData = dynamoDBMapper.query(DataTableDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return sessionData;
    }

    public int getTotalSessions( final DynamoDBMapper dynamoDBMapper, final DataTableDO dataTableDO ) throws InterruptedException {
        int index = 0;
        PaginatedQueryList<DataTableDO> resList;
        while( true ){
            resList = getSessionData( dynamoDBMapper, dataTableDO, index+1 );
            if( resList == null || resList.size() == 0 ){
                break;
            }
            else {
                index = index + 1;
            }
        }
        return index;
    }

}
