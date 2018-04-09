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

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-Overall_Patient_Performance_Table")

public class OverallPatientPerformanceTableDO {
    private String _pUser;
    private String _session;
    private Map<String, String> _data;
    private PaginatedQueryList<OverallPatientPerformanceTableDO> sessionData;
    private PaginatedQueryList<OverallPatientPerformanceTableDO> overallPatientData;

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
    /////////////////////////////////////


    public void readItem(final DynamoDBMapper dynamoDBMapper, final String hashKey, final String rangeKey ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                OverallPatientPerformanceTableDO dd = dynamoDBMapper.load( OverallPatientPerformanceTableDO.class, hashKey, rangeKey );
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


    public void updateItem( final DynamoDBMapper dynamoDBMapper, final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO, final Map<String, String> data ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                DataTableDO dd = dynamoDBMapper.load( DataTableDO.class, "("+overallPatientPerformanceTableDO.getPUser()+")" , "("+overallPatientPerformanceTableDO.getSession()+")" );
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


    public void deleteItem(final DynamoDBMapper dynamoDBMapper, final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO ) {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.delete( overallPatientPerformanceTableDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public PaginatedQueryList<OverallPatientPerformanceTableDO> getSessionData(final DynamoDBMapper dynamoDBMapper, final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO, final int session ) throws InterruptedException {
        String temp = Integer.toString( session );
        final String sess = "(" + temp;
        Runnable runnable = new Runnable() {
            public void run() {
                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(sess.toString()));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(overallPatientPerformanceTableDO)
                        .withRangeKeyCondition("session", rangeKeyCondition)
                        .withConsistentRead(false);

                sessionData = dynamoDBMapper.query(OverallPatientPerformanceTableDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return sessionData;
    }


    public PaginatedQueryList<OverallPatientPerformanceTableDO> getAllUserData( final DynamoDBMapper dynamoDBMapper, final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO ) throws InterruptedException {
        final String sess = "(";
        Runnable runnable = new Runnable() {
            public void run() {
                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(sess.toString()));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(overallPatientPerformanceTableDO)
                        .withRangeKeyCondition("session", rangeKeyCondition)
                        .withConsistentRead(false);

                overallPatientData = dynamoDBMapper.query(OverallPatientPerformanceTableDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return overallPatientData;
    }

    public int getTotalSessions( final DynamoDBMapper dynamoDBMapper, final OverallPatientPerformanceTableDO overallPatientPerformanceTableDO ) throws InterruptedException {
        int index = 0;
        PaginatedQueryList<OverallPatientPerformanceTableDO> resList = getAllUserData( dynamoDBMapper, overallPatientPerformanceTableDO );
        if( resList == null || resList.size() == 0 )
            index = 0;
        else
            index = resList.size();
        return index;
    }


    public String[][] getStrideDataDisplay( PaginatedQueryList<OverallPatientPerformanceTableDO> list ){
        String[] dates = new String[list.size()];
        String[] session = new String[list.size()];
        String[] avg = new String[list.size()];
        String[] goal = new String[list.size()];

        int index = 0;
        while( index < list.size() ){
            OverallPatientPerformanceTableDO cur_data = list.get(index);
            Map<String, String> map = cur_data.getData();
            String temp_session = map.get( "sessionID" );
            session[index] =temp_session;
            dates[index] = map.get("Date");
            String temp_average0 = map.get("AvgStrideLength");
            float temp_average1 = Float.parseFloat(temp_average0);
            temp_average0 = String.format( "%.2f", temp_average1 );
            avg[index] = temp_average0;
            float temp_goal0 = (float)(Math.random());
            String temp_goal1 = String.format( "%.2f", temp_goal0 );
            goal[index] = temp_goal1;
            index++;
        }

        String[][] result = new String[4][goal.length];
        result[0] = dates;
        result[1] = session;
        result[2] = avg;
        result[3] = goal;

        return result;
    }


    public String[][] getHeelToeDataDisplay( PaginatedQueryList<OverallPatientPerformanceTableDO> list ){
        String[] dates = new String[list.size()];
        String[] session = new String[list.size()];
        String[] avg = new String[list.size()];
        String[] goal = new String[list.size()];

        int index = 0;
        while( index < list.size() ){
            OverallPatientPerformanceTableDO cur_data = list.get(index);
            Map<String, String> map = cur_data.getData();
            String temp_session = map.get( "sessionID" );
            session[index] =temp_session;
            dates[index] = map.get("Date");
            String temp_average0 = map.get("AvgHeelToe");
            float temp_average1 = Float.parseFloat(temp_average0);
            temp_average0 = String.format( "%.2f", temp_average1 );
            avg[index] = temp_average0;
            float temp_goal0 = (float)(Math.random());
            String temp_goal1 = String.format( "%.2f", temp_goal0 );
            goal[index] = temp_goal1;
            index++;
        }

        String[][] result = new String[4][goal.length];
        result[0] = dates;
        result[1] = session;
        result[2] = avg;
        result[3] = goal;

        return result;
    }


    public String[][] getCadenceDataDisplay( PaginatedQueryList<OverallPatientPerformanceTableDO> list ){
        String[] dates = new String[list.size()];
        String[] session = new String[list.size()];
        String[] avg = new String[list.size()];
        String[] goal = new String[list.size()];

        int index = 0;
        while( index < list.size() ){
            OverallPatientPerformanceTableDO cur_data = list.get(index);
            Map<String, String> map = cur_data.getData();
            String temp_session = map.get( "sessionID" );
            session[index] =temp_session;
            dates[index] = map.get("Date");
            String temp_average0 = map.get("Cadence");
            avg[index] = temp_average0;
            float temp_goal0 = (float)(Math.random());
            String temp_goal1 = String.format( "%.2f", temp_goal0 );
            goal[index] = temp_goal1;
            index++;
        }

        String[][] result = new String[4][goal.length];
        result[0] = dates;
        result[1] = session;
        result[2] = avg;
        result[3] = goal;

        return result;
    }


    public String[][] getFreezingDataDisplay( PaginatedQueryList<OverallPatientPerformanceTableDO> list ){
        String[] dates = new String[list.size()];
        String[] session = new String[list.size()];
        String[] avg = new String[list.size()];
        String[] goal = new String[list.size()];

        int index = 0;
        while( index < list.size() ){
            OverallPatientPerformanceTableDO cur_data = list.get(index);
            Map<String, String> map = cur_data.getData();
            String temp_session = map.get( "sessionID" );
            session[index] =temp_session;
            dates[index] = map.get("Date");
            String temp_average0 = map.get("Freezing");
            float temp_average1 = Float.parseFloat(temp_average0);
            temp_average0 = String.format( "%.2f", temp_average1 );
            avg[index] = temp_average0;
            float temp_goal0 = (float)(Math.random());
            String temp_goal1 = String.format( "%.2f", temp_goal0 );
            goal[index] = temp_goal1;
            index++;
        }

        String[][] result = new String[4][goal.length];
        result[0] = dates;
        result[1] = session;
        result[2] = avg;
        result[3] = goal;

        return result;
    }
}
