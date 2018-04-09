package com.example.richie.stride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Patient_First_Screen extends AppCompatActivity {

    private Button viewWorkouts;
    private Button viewPerformance;
    private Button editInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__first__screen);

        Intent in = getIntent();
        final String cur_patient = in.getStringExtra( "com.example.richie.CURRENT_PATIENT" );

        android.support.v7.widget.Toolbar logoToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.logoToolBar);
        setSupportActionBar(logoToolBar);
        getSupportActionBar().setTitle("");

        viewWorkouts = (Button) findViewById( R.id.ViewWorkouts );
        viewPerformance = (Button) findViewById( R.id.ViewPerformanceButton );
        editInfo = (Button) findViewById( R.id.EditInfoButton );

        viewWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_First_Screen.this, Patient_View_Workouts.class );
                intent.putExtra("com.example.richie.CURRENT_PATIENT", cur_patient );
                startActivity( intent );
            }
        });

        viewPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_First_Screen.this, Patient_Performance_Options.class );
                intent.putExtra("com.example.richie.CURRENT_PATIENT", cur_patient );
                startActivity( intent );
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Patient_First_Screen.this, Patient_Edit_Info.class );
                intent.putExtra("com.example.richie.CURRENT_PATIENT", cur_patient );
                startActivity( intent );
            }
        });
    }
}
