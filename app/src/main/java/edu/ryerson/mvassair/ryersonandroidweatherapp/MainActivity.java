package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LinearLayout weatherLine;
    RecyclerView weatherList;
    RecyclerView.Adapter recycleAdapter;
    RecyclerView.LayoutManager recycleMan;
    View separator;
    OWMHandler owm;
    RequestQueue queue;

    //Example request URL "http://samples.openweathermap.org/data/2.5/weather?id=2172797&units=metric&appid=b6907d289e10d714a6e88b30761fae22"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        owm = new OWMHandler(this);
        queue = Volley.newRequestQueue(this);

        weatherLine = findViewById(R.id.WeatherLine);
        weatherList = findViewById(R.id.WeatherList);
        separator = findViewById(R.id.separator);

        weatherList.setHasFixedSize(true);
        recycleMan = weatherList.getLayoutManager();
        recycleAdapter = weatherList.getAdapter();

        weatherList.setLayoutManager(recycleMan);
        weatherList.setAdapter(recycleAdapter);

        owm.makeRequest(getString(R.string.apisample) + getString(R.string.apikey), queue);


        addNewLines (13);

    }

    //Called by the OWMHandler when a response is gotten
    protected void sendData(JSONObject data){
        //Inflate a new line from the template
        View newline = LayoutInflater.from(this).inflate(R.layout.weatherline, weatherList, false);
        try {
            //Get the components of the weather line
            ImageView weather_image = newline.findViewById(R.id.WeatherImage);
            TextView line_text = newline.findViewById(R.id.WeatherString);
            TextView loc_text = newline.findViewById(R.id.Location);
            //Set the weather line info based on JSON received
            weather_image.setImageResource(R.drawable.ic_placeholder_cloud);
            line_text.setText("Current temperature: " + (int)data.getJSONObject("main").getDouble("temp") + getString(R.string.celsius));
            loc_text.setText(data.getString("name"));
        }catch(JSONException e){
            System.out.println("JSON Key not found... probably\n" + e.getMessage());
        }
        weatherList.addView(newline);
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
