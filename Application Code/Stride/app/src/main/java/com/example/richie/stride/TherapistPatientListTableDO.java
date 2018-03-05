package com.example.richie.stride;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-Therapist_Patient_List_Table")

public class TherapistPatientListTableDO {
    private String _tUser;
    private String _pUser;
    private PaginatedQueryList<TherapistPatientListTableDO> patients;


    @DynamoDBHashKey(attributeName = "t_user")
    @DynamoDBAttribute(attributeName = "t_user")
    public String getTUser() {
        return _tUser;
    }

    public void setTUser(final String _tUser) {
        this._tUser = _tUser;
    }

    @DynamoDBRangeKey(attributeName = "p_user")
    @DynamoDBAttribute(attributeName = "p_user")
    public String getPUser() {
        return _pUser;
    }

    public void setPUser(final String _pUser) {
        this._pUser = _pUser;
    }
    //////////////////////////////////////////////


    public PaginatedQueryList<TherapistPatientListTableDO> getPatientList(final DynamoDBMapper dynamoDBMapper, final TherapistPatientListTableDO therapistPatientListTableDO, final String therapist ) throws InterruptedException {
        final String sess = "(" + therapist + "_";
        Runnable runnable = new Runnable() {
            public void run() {
                /*Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(therapist.toString()));*/

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(therapistPatientListTableDO)
                        //.withRangeKeyCondition("sessionID", rangeKeyCondition)
                        .withConsistentRead(false);

                patients = dynamoDBMapper.query(TherapistPatientListTableDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return patients;
    }

}
