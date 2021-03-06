package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Therapist_View_Patients extends AppCompatActivity {

    private ListView patientListView;
    private EditText editSearch;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton add_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__view__patients);

        Context appContext = getApplicationContext();
        Intent in = getIntent();

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

        final String cur_therapist = in.getStringExtra( "com.example.richie.CURRENT_THERAPIST" );

        //call method to get names of all therapist patients
        //section gets names from Therapist_Patient_List database and puts them in list
        TherapistPatientListTableDO table = new TherapistPatientListTableDO();
        table.setTUser( cur_therapist );
        PaginatedQueryList<TherapistPatientListTableDO> patientList = null;
        try {
            patientList = table.getPatientList( dynamoDBMapper, table, table.getTUser() );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int numPatients = patientList.size();
        final String[] patient_names = new String[numPatients];

        if( patientList == null || patientList.size() == 0 ){
            System.out.println("###################\nDID NOT WORK\n##################");
        }
        else{
            int index = 0;
            while( index < patientList.size() ){
                TherapistPatientListTableDO curPatient = patientList.get( index );
                patient_names[index] = curPatient.getPUser();
                index++;
            }
        }
        /////////////////////////////////////////////////////

        patientListView = (ListView) findViewById(R.id.patientListView);
        editSearch = (EditText) findViewById(R.id.editSearch);
        add_patient = (FloatingActionButton) findViewById(R.id.fab_add_patient);
        adapter = new ArrayAdapter<String>(this, R.layout.patient_list, R.id.textView, patient_names );
        patientListView.setAdapter(adapter);

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent( Therapist_View_Patients.this, Therapist_Add_Patient.class );
                in.putExtra( "com.example.richie.CURRENT_THERAPIST", cur_therapist );
                System.out.println( "^^^^^^^^^" );
                System.out.println( "Therapist_View_patients" );
                startActivity( in );
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Therapist_View_Patients.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showTherapistOptions = new Intent( getApplicationContext(), Therapist_Second_Screen.class );
                String selected_patient = patient_names[i];
                showTherapistOptions.putExtra( "com.example.richie.PATIENT_INDEX", selected_patient );
                showTherapistOptions.putExtra( "com.example.richie.CUR_THERAPIST", cur_therapist );
                startActivity( showTherapistOptions );
            }
        });
    }
}
