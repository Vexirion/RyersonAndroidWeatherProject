package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout weatherLine;
    LinearLayout weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherLine = (LinearLayout) findViewById(R.id.WeatherLine);
        weatherList = (LinearLayout) findViewById(R.id.WeatherList);

        addNewLines (weatherLine, 3);

    }

    //test function
    private void addNewLines(LinearLayout line, int amnt){

        for (int i = 0; i < amnt; i++){

            View newline = LayoutInflater.from(this).inflate(R.layout.weatherline, null);
            weatherList.addView(newline);

        }

    }
}
