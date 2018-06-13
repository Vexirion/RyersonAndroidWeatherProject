package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    RecyclerView weatherList;
    WeatherAdapter recycleAdapter;
    RecyclerView.LayoutManager recycleMan;
    View separator;
    OWMHandler owm;
    RequestQueue queue;
    ArrayList<DBWeatherInfo> weatherData;
    public static ArrayList<DBLocation> locations;
    public WeatherDB DB;
    Button settings;

    //Example request URL "http://samples.openweathermap.org/data/2.5/weather?id=2172797&units=metric&appid=b6907d289e10d714a6e88b30761fae22"

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up Volley
        owm = new OWMHandler(this);
        queue = Volley.newRequestQueue(this);

        //Set up DB and data holders
        DB = new WeatherDB(this);
        weatherData = new ArrayList<>();
        locations = new ArrayList<>();

        //Pull Weather info from DB
        DBOps operator = new DBOps(DB);
        operator.setReadMode(DBOP.DATAREAD);
        operator.execute();
        try {
            ArrayList L = operator.get();
            if (L.get(0) instanceof DBWeatherInfo)
                weatherData = new ArrayList<DBWeatherInfo>(L);
            else
                System.out.println("Could not get Weather data from DB");
        }catch(ExecutionException | InterruptedException e){e.printStackTrace();}

        //pull location info from DB
        DBOps operator2 = new DBOps(DB);
        operator2.setReadMode(DBOP.LOCREADALL);
        operator2.execute();
        try{
            ArrayList L = operator2.get();
            if (L.get(0) instanceof DBLocation)
                locations = new ArrayList<DBLocation>(L);
            else
                System.out.println("Could not get Locations from DB");
        }catch (ExecutionException | InterruptedException e){e.printStackTrace();}

        //Set up RecyclerView
        weatherList = findViewById(R.id.WeatherList);
        weatherList.setHasFixedSize(true);
        recycleMan = new LinearLayoutManager(this);
        recycleAdapter = new WeatherAdapter(weatherData); //Data is initially empty
        weatherList.setLayoutManager(recycleMan);
        weatherList.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Settings button was clicked");
                if (!MainActivity.locations.isEmpty()){
                    Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(i);
                } else
                    System.out.println("Locations list is empty!");
            }
        });

        //owm.makeRequest will populate weatherData with fresh OWM data
        //owm.makeRequest(getString(R.string.apisample) + getString(R.string.apikey), queue);
        System.out.println("End of OnCreate");

    }

    //Called by the OWMHandler when a response is gotten
    //This section will need to be modified once RecyclerView is implemented
    protected void sendData(JSONObject data){

        DBWeatherInfo dataline = null;

        //Grab the info we need out of the OWM response, pack it together, and put it in the data set
        try{
            int temp = (int)data.getJSONObject("main").getDouble("temp");
            String loc = data.getString("name");
            int condition = data.getJSONArray("weather").getJSONObject(0).getInt("id");
            Timestamp time = new Timestamp(data.getLong("dt"));
            int id = data.getInt("id");

            //OpenWeatherMap represents weather conditions with ranges of IDs based on categories
            if (id >= 200 && id < 300) //Thunderstorm
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.THUNDER, time, loc);
            else if (id >= 300 && id  < 600) //Drizzle and Rain. Something may be added to the 400 range eventually
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.RAIN, time, loc);
            else if (id >= 600 && id < 700) //Fog or other atmospheric conditions
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.FOG, time, loc);
            else if (id == 800 || id == 801) //Clear
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.SUN, time, loc);
            else if (id > 801) //Cloudy
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.CLOUDS, time, loc);
            else{ //anything else
                dataline = new DBWeatherInfo(id, temp, condition, WeatherCondition.OTHER, time, loc);
                System.out.println("An unrecognized weather ID was received: " + id);
            }

            //If the weather data we got is already in the list, update it and return
            for (DBWeatherInfo w : weatherData){
                if (dataline.id == w.id){
                    w = dataline;
                    recycleAdapter.notifyItemChanged(weatherData.indexOf(w));
                    return;
                }
            }
            //If we made it through the above loop without returning, the ID we got isn't in the list, so add it
            weatherData.add(dataline);
            recycleAdapter.notifyItemInserted(weatherData.size()-1);

        }catch(JSONException e){
            System.out.println("One or more JSON keys were not found.\n" + e.getMessage());
        } finally{
            if (dataline == null)
                System.out.println("Something somewhere along the lime broke, and you need to fix it");
        }
    }
}
