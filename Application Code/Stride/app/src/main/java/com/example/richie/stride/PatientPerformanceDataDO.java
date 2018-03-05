package com.example.richie.stride;

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

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-Patient_Performance_Data")

public class PatientPerformanceDataDO {
    private String _pUser;
    private String _session;
    private Map<String, String> _data;
    private PaginatedQueryList<PatientPerformanceDataDO> sessionData;

    @DynamoDBHashKey(attributeName = "p_user")
    @DynamoDBAttribute(attributeName = "p_user")
    public String getPUser() {
        return _pUser;
    }

    public void setPUser(final String _pUser) {
        this._pUser = _pUser;
    }
    @DynamoDBRangeKey(attributeName = "session")
    @DynamoDBAttribute(attributeName = "session")
    public String getSession() {
        return _session;
    }

    public void setSession(final String _session) {
        this._session = _session;
    }
    @DynamoDBAttribute(attributeName = "data")
    public Map<String, String> getData() {
        return _data;
    }

    public void setData(final Map<String, String> _data) {
        this._data = _data;
    }
    ////////////////////////////////


    public void readItem(final DynamoDBMapper dynamoDBMapper, final String hashKey, final String rangeKey ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                PatientPerformanceDataDO dd = dynamoDBMapper.load( PatientPerformanceDataDO.class, hashKey, rangeKey );
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


    public void updateItem( final DynamoDBMapper dynamoDBMapper, final PatientPerformanceDataDO patientPerformanceDataDO, final Map<String, String> data ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                DataTableDO dd = dynamoDBMapper.load( DataTableDO.class, "("+patientPerformanceDataDO.getPUser()+")" , "("+patientPerformanceDataDO.getSession()+")" );
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


    public void deleteItem(final DynamoDBMapper dynamoDBMapper, final PatientPerformanceDataDO patientPerformanceDataDO ) {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.delete( patientPerformanceDataDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public PaginatedQueryList<PatientPerformanceDataDO> getSessionData(final DynamoDBMapper dynamoDBMapper, final PatientPerformanceDataDO patientPerformanceDataDO, final int session ) throws InterruptedException {
        String temp = Integer.toString( session );
        final String sess = "(" + temp + "_";
        Runnable runnable = new Runnable() {
            public void run() {
                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(sess.toString()));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(patientPerformanceDataDO)
                        .withRangeKeyCondition("session", rangeKeyCondition)
                        .withConsistentRead(false);

                sessionData = dynamoDBMapper.query(DataTableDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return sessionData;
    }

    public int getTotalSessions( final DynamoDBMapper dynamoDBMapper, final PatientPerformanceDataDO patientPerformanceDataDO ) throws InterruptedException {
        int index = 0;
        PaginatedQueryList<PatientPerformanceDataDO> resList;
        while( true ){
            resList = getSessionData( dynamoDBMapper, patientPerformanceDataDO, index+1 );
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
