package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    OWMHandler owm;
    RequestQueue queue;
    public static ArrayList<DBWeatherInfo> weatherData;
    public static ArrayList<DBLocation> locations;
    public static WeatherDB DB;
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
        //Because these values may persist through other activities being opened, check these to see if they exist before initializing them
        if (weatherData == null)
            weatherData = new ArrayList<>();
        if(locations == null)
            locations = new ArrayList<>();

        //pull location info from DB
        DBOps operator2 = new DBOps(DB);
        operator2.setReadMode(DBOP.LOCREADALL);
        operator2.execute();
        try{
            ArrayList<DBLocation> L = operator2.get();
            if (L.get(0) != null)
                locations = new ArrayList<>(L);
            else
                System.out.println("Could not get Locations from DB");
        }catch (ExecutionException | InterruptedException e){e.printStackTrace();}

        //Pull Weather info from DB
        DBOps operator = new DBOps(DB);
        operator.setReadMode(DBOP.DATAREAD);
        operator.execute();
        try {
            ArrayList<DBWeatherInfo> L = operator.get();
            ArrayList<DBWeatherInfo> toRemove = new ArrayList<>();
            if (L.get(0) != null && weatherData.isEmpty())
                weatherData = new ArrayList<>(L);
            if(L.get(0) != null && !weatherData.isEmpty()){
                for(DBWeatherInfo OLD: weatherData){
                    for(DBWeatherInfo NEW: L){ //This will probably never come up, but it doesn't hurt to check if the DB somehow has newer information
                        if(OLD.id == NEW.id && NEW.lastupdate.getTime() > OLD.lastupdate.getTime()) {
                            OLD = NEW;
                            break;
                        }
                    }
                    for(DBLocation loc : locations){
                        if(loc.id == OLD.id && loc.selected < 1) //If we have weather data for a location, and it isn't being tracked anymore, remove it
                            toRemove.add(OLD);
                    }
                }
                weatherData.removeAll(toRemove);
            }
            else
                System.out.println("Could not get Weather data from DB");
        }catch(ExecutionException | InterruptedException e){e.printStackTrace();}

        //Set up RecyclerView
        weatherList = findViewById(R.id.WeatherList);
        weatherList.setHasFixedSize(true);
        recycleMan = new LinearLayoutManager(this);
        recycleAdapter = new WeatherAdapter(weatherData);
        weatherList.setLayoutManager(recycleMan);
        weatherList.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.locations.isEmpty()){
                    Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(i);
                }
            }
        });

        //Check if we have network available
        ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = conman.getActiveNetworkInfo();
        //If network is available, build the list of IDs to update
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            ArrayList<Integer> IDs = new ArrayList<>();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            //If the ID is selected for tracking, add it to the list of IDs to update
            for (DBLocation loc : locations){
                boolean wasAdded = false;
                if (loc.selected <= 0)
                    continue;
                //Since the last update time is stored in weatherdata, we need to loop through that
                for(DBWeatherInfo w : weatherData){
                    if (loc.id == w.id && now.getTime() - w.lastupdate.getTime() >= 600000) {
                        System.out.println("Time since last update in Millis " + (now.getTime() - w.lastupdate.getTime()) + "\nUpdating " + w.id);
                        IDs.add(loc.id);
                        wasAdded = true;
                        break;
                    }
                }
                if(!wasAdded) {
                    IDs.add(loc.id); //If we made it down here, it means we're tracking this location, but it doesn't have data yet
                }
            }
            //owm.makeRequest will populate weatherData with fresh OWM data
            owm.makeRequest(getString(R.string.APIURL) + getString(R.string.APPID), queue, IDs);
        }
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
            Timestamp time = new Timestamp(data.getLong("dt")*1000);
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
            }

            //If the weather data we got is already in the list, update it and return
            for (DBWeatherInfo w : weatherData){
                if (dataline.id == w.id){
                    weatherData.set(weatherData.indexOf(w), dataline);
                    recycleAdapter.notifyDataSetChanged();
                    return;
                }
            }
            //If we made it through the above loop without returning, the ID we got isn't in the list, so add it
            weatherData.add(dataline);
            recycleAdapter.notifyDataSetChanged();

        }catch(JSONException e){
            System.out.println("One or more JSON keys were not found.\n" + e.getMessage());
        }
    }

    protected void onStop(){
        super.onStop();
        DBOps savedata = new DBOps(DB);
        savedata.setWeatherUpdate(weatherData);
        savedata.execute();
    }
}
