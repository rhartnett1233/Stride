package com.example.richie.stride;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-PatientWorkoutList")

public class PatientWorkoutListDO {
    private String _patient;
    private String _workout;
    private String _bpm;
    private String _type;
    private String _value;
    private PaginatedQueryList<PatientWorkoutListDO> workoutList;

    @DynamoDBHashKey(attributeName = "patient")
    @DynamoDBAttribute(attributeName = "patient")
    public String getPatient() {
        return _patient;
    }

    public void setPatient(final String _patient) {
        this._patient = _patient;
    }
    @DynamoDBRangeKey(attributeName = "workout")
    @DynamoDBAttribute(attributeName = "workout")
    public String getWorkout() {
        return _workout;
    }

    public void setWorkout(final String _workout) {
        this._workout = _workout;
    }
    @DynamoDBAttribute(attributeName = "bpm")
    public String getBpm() {
        return _bpm;
    }

    public void setBpm(final String _bpm) {
        this._bpm = _bpm;
    }
    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return _type;
    }

    public void setType(final String _type) {
        this._type = _type;
    }
    @DynamoDBAttribute(attributeName = "value")
    public String getValue() {
        return _value;
    }

    public void setValue(final String _value) {
        this._value = _value;
    }

    public PaginatedQueryList<PatientWorkoutListDO> getWorkoutList(final DynamoDBMapper dynamoDBMapper, final PatientWorkoutListDO patientWorkoutListDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(patientWorkoutListDO)
                        //.withRangeKeyCondition("session", rangeKeyCondition)
                        .withConsistentRead(false);

                workoutList = dynamoDBMapper.query(PatientWorkoutListDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return workoutList;
    }

    public void createItem( final DynamoDBMapper dynamoDBMapper, String patient, String workout, String type, String value, String bpm ) throws InterruptedException {
        final PatientWorkoutListDO patientWorkoutListDO = new PatientWorkoutListDO();
        patientWorkoutListDO.setPatient(patient);
        patientWorkoutListDO.setWorkout(workout);
        patientWorkoutListDO.setType(type);
        patientWorkoutListDO.setValue(value);
        patientWorkoutListDO.setBpm( bpm );

        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.save( patientWorkoutListDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void createItem(final DynamoDBMapper dynamoDBMapper, final PatientWorkoutListDO patientWorkoutListDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.save( patientWorkoutListDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }

}
