package com.example.richie.stride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Therapist_Performace_Options extends AppCompatActivity {

    Button strideLengthButton;
    Button cadenceButton;
    Button heelToeButton;
    Button freezingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__performace__options);

        Intent in = getIntent();

        android.support.v7.widget.Toolbar logoToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.logoToolBar);
        setSupportActionBar(logoToolBar);
        getSupportActionBar().setTitle("");

        final String curPatient = in.getStringExtra( "com.example.richie.PATIENT_INDEX" );

        strideLengthButton = (Button) findViewById( R.id.StrideLengthButton );
        cadenceButton = (Button) findViewById( R.id.CadenceButton );
        heelToeButton = (Button) findViewById( R.id.HeelToeButton );
        freezingButton = (Button) findViewById( R.id.FreezingButton );

        strideLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Performace_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Stride" );
                startActivity( intent );
            }
        });

        cadenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Performace_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Cadence" );
                startActivity( intent );
            }
        });

        heelToeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Performace_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Heel" );
                startActivity( intent );
            }
        });

        freezingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Performace_Options.this, Therapist_Performance_Data.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.DATA_TYPE", "Freezing" );
                startActivity( intent );
            }
        });

    }
}
