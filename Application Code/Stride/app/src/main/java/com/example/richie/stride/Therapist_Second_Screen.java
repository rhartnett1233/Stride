package com.example.richie.stride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Therapist_Second_Screen extends AppCompatActivity {

    Button programExerciseButton;
    Button viewPatientPerformanceButton;
    Button editPatientInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__second__screen);

        Intent in = getIntent();

        android.support.v7.widget.Toolbar logoToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.logoToolBar);
        setSupportActionBar(logoToolBar);
        getSupportActionBar().setTitle("");

        final String curPatient = in.getStringExtra( "com.example.richie.PATIENT_INDEX" );
        final String cur_therapist = in.getStringExtra( "com.example.richie.CUR_THERAPIST");

        programExerciseButton = (Button) findViewById( R.id.ProgramExerciseButton );
        viewPatientPerformanceButton = (Button) findViewById( R.id.ViewPatientPerformanceButton );
        editPatientInfoButton = (Button) findViewById( R.id.EditPatientInfoButton );

        programExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Second_Screen.this, Therapist_Program_Workout.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.CUR_THERAPIST", cur_therapist );
                startActivity( intent );
            }
        });

        viewPatientPerformanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Second_Screen.this, Therapist_Performace_Options.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.CUR_THERAPIST", cur_therapist );
                startActivity( intent );
            }
        });

        editPatientInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Therapist_Second_Screen.this, Therapist_Edit_Patient_Info.class );
                intent.putExtra("com.example.richie.PATIENT_INDEX", curPatient );
                intent.putExtra( "com.example.richie.CUR_THERAPIST", cur_therapist );
                startActivity( intent );
            }
        });




    }
}
