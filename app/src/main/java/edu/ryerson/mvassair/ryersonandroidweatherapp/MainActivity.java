package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout weatherLine;
    LinearLayout weatherList;
    View separator;
    OWMHandler owm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: put OWMHandler into a thread
        owm = new OWMHandler();

        weatherLine = findViewById(R.id.WeatherLine);
        weatherList = findViewById(R.id.WeatherList);
        separator = findViewById(R.id.separator);

        addNewLines (13);

    }

    //test function
    private void addNewLines(int amnt){

        for (int i = 0; i < amnt; i++){

            View newline = LayoutInflater.from(this).inflate(R.layout.weatherline, weatherList, false);

            ImageView weather_image = newline.findViewById(R.id.WeatherImage);
            TextView line_text = newline.findViewById(R.id.WeatherString);
            TextView loc_text = newline.findViewById(R.id.Location);
            weather_image.setImageResource(R.drawable.ic_placeholder_rain);
            line_text.setText("This is loop #" + i);
            loc_text.setText("Location" + i);

            weatherList.addView(newline);

        }

    }
}
