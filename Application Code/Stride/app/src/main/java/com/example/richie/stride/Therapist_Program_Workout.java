package com.example.richie.stride;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class Therapist_Program_Workout extends AppCompatActivity {

    private ListView workoutListView;
    private EditText editSearch;
    private ArrayAdapter<String> adapter;

    String data[] = { "Walk 50 Steps", "Walk 100 Steps", "Walk 30 Seconds", "Walk 1 Minute" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__program__workout);

        workoutListView = (ListView) findViewById(R.id.workoutListView);
        editSearch = (EditText) findViewById(R.id.editSearch);
        adapter = new ArrayAdapter<String>(this, R.layout.workout_list, R.id.nameTextView, data );
        workoutListView.setAdapter(adapter);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Therapist_Program_Workout.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
