package com.example.richie.stride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Patient_Performance_Options extends AppCompatActivity {

    private Button strideLengthButton;
    private Button cadenceButton;
    private Button heelToeButton;
    private Button freezingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__performance__options);

        Intent in = getIntent();

        android.support.v7.widget.Toolbar logoToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.logoToolBar);
        setSupportActionBar(logoToolBar);
        getSupportActionBar().setTitle("");

        final String cur_patient = in.getStringExtra( "com.example.richie.CURRENT_PATIENT" );

        strideLengthButton = (Button) findViewById( R.id.PatientStrideLengthButton );
        cadenceButton = (Button) findViewById( R.id.PatientCadenceButton );
        heelToeButton = (Button) findViewById( R.id.PatientHeelToeButton );
        freezingButton = (Button) findViewById( R.id.PatientFreezingButton );

        strideLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_Performance_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", cur_patient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Stride" );
                startActivity( intent );
            }
        });

        cadenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_Performance_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", cur_patient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Cadence" );
                startActivity( intent );
            }
        });

        heelToeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_Performance_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", cur_patient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Heel" );
                startActivity( intent );
            }
        });

        freezingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_Performance_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", cur_patient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Freezing" );
                startActivity( intent );
            }
        });
    }
}
