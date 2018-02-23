package com.example.richie.stride;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Therapist_Performance_Data extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist__performance__data);

        lineChart = (LineChart) findViewById( R.id.lineChart );

        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesSin = new ArrayList<>();
        ArrayList<Entry> yAxesCos = new ArrayList<>();

        double x = 0;
        int numDataPoints = 1000;
        for( int i = 0; i < numDataPoints; i++ ){
            float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunction = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x + 0.1;
            yAxesSin.add( new Entry(i, sinFunction) );
            yAxesCos.add( new Entry(i, cosFunction) );
            xAxes.add( i, String.valueOf(x) );
        }

        String[] xaxes = new String[xAxes.size()];
        for( int i = 0; i < xAxes.size(); i++ ){
            xaxes[i] = xAxes.get(i).toString();
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAxesCos, "cos");
        lineDataSet1.setDrawCircles( false );
        lineDataSet1.setColor( Color.BLUE );

        LineDataSet lineDataSet2 = new LineDataSet(yAxesSin, "sin");
        lineDataSet2.setDrawCircles( false );
        lineDataSet2.setColor( Color.RED );

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData( new LineData(lineDataSets) );
        //lineChart.setRotation(90);

        lineChart.setVisibleXRangeMaximum(65f);

        String[] date = { "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun" };
        String[] w = { "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun" };
        String[] y = { "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun" };
        String[] z = { "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun" };

        ListView list = (ListView) findViewById(R.id.listView);
        Therapist_Data_Adapter adapter = new Therapist_Data_Adapter(this, date, w, y, z);
        list.setAdapter( adapter );

    }
}
