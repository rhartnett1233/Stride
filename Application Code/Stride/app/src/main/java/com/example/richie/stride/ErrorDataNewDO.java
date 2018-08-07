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

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-Error_Data_New")

public class ErrorDataNewDO {
    private String _user;
    private String _sessionID;
    private Map<String, String> _data;
    private PaginatedQueryList<ErrorDataNewDO> error_list;

    @DynamoDBHashKey(attributeName = "user")
    @DynamoDBAttribute(attributeName = "user")
    public String getUser() {
        return _user;
    }

    public void setUser(final String _user) {
        this._user = _user;
    }
    @DynamoDBRangeKey(attributeName = "sessionID")
    @DynamoDBAttribute(attributeName = "sessionID")
    public String getSessionID() {
        return _sessionID;
    }

    public void setSessionID(final String _sessionID) {
        this._sessionID = _sessionID;
    }
    @DynamoDBAttribute(attributeName = "data")
    public Map<String, String> getData() {
        return _data;
    }

    public void setData(final Map<String, String> _data) {
        this._data = _data;
    }


    public String checkError(DynamoDBMapper dynamoDBMapper, String user, final int session ) throws InterruptedException {
        ErrorDataNewDO errorDataNewDO = new ErrorDataNewDO();
        errorDataNewDO.setUser( "(" + user + ")" );
        String res = "";
        getSessionData( dynamoDBMapper, errorDataNewDO, session );
        if( error_list.size() > 0 ){
            errorDataNewDO = error_list.get(0);
            Map<String, String> temp_map = errorDataNewDO.getData();
            String is_error = temp_map.get("Error");
            if( is_error.equals("0") ){
                res = "WORKOUT COMPLETE";
            }
            else{
                res = temp_map.get("Type");
            }
        }
        return res;
    }


    public PaginatedQueryList<ErrorDataNewDO> getSessionData(final DynamoDBMapper dynamoDBMapper, final ErrorDataNewDO errorDataNewDO, final int session ) throws InterruptedException {
        String temp = Integer.toString( session );
        final String sess = "(" + temp;
        Runnable runnable = new Runnable() {
            public void run() {
                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(sess.toString()));

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(errorDataNewDO)
                        .withRangeKeyCondition("sessionID", rangeKeyCondition)
                        .withConsistentRead(false);

                error_list = dynamoDBMapper.query(ErrorDataNewDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return error_list;
    }

}
