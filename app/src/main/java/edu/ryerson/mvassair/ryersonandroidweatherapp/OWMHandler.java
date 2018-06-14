package edu.ryerson.mvassair.ryersonandroidweatherapp;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class OWMHandler{

    private JsonObjectRequest req;
    MainActivity parent;

    OWMHandler(MainActivity parent){
        req = null;
        this.parent = parent;
    }

    //Volley is automatically threaded out for you, so you don't need to fiddle with AsyncTasks
    protected void makeRequest(String urlsrc, RequestQueue queue, ArrayList<Integer> IDs){

        for(int id : IDs) {
            String urlfinal = String.format(urlsrc, id);
            System.out.println(urlfinal);

            //Volley JSON Array request. parameters are: HTTP type, URL to access, JSON to send with request, Response listener, Error listener
            req = new JsonObjectRequest(Request.Method.GET, urlfinal, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    if (checkJSON(response))
                        parent.sendData(response);
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.add(req);
        }
    }

    //It looks like Volley already automatically parses all the JSON into dicts and values
    //So we just have to use them as-is. This function currently exists to be a breakpoint for debugger inspection
    private boolean checkJSON(JSONObject JSON){

        try {
            if (JSON.has("error")) {
                return false;
            }
            else if (JSON.has ("cod") && JSON.getInt("cod") == 429){
                return false;
            }
            else {
                return true;
            }
        }catch (JSONException j){}
    return false; //If the Try fails, something went horribly wrong. We probably didn't receive JSON somehow
    }
}


/*
coord
    coord.lon City geo location, longitude
    coord.lat City geo location, latitude
weather (more info Weather condition codes)
    weather.id Weather condition id
    weather.main Group of weather parameters (Rain, Snow, Extreme etc.)
    weather.description Weather condition within the group
    weather.icon Weather icon id
base Internal parameter
main
    main.temp Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    main.pressure Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level weatherData), hPa
    main.humidity Humidity, %
    main.temp_min Minimum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    main.temp_max Maximum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    main.sea_level Atmospheric pressure on the sea level, hPa
    main.grnd_level Atmospheric pressure on the ground level, hPa
wind
    wind.speed Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    wind.deg Wind direction, degrees (meteorological)
clouds
    clouds.all Cloudiness, %
rain
    rain.3h Rain volume for the last 3 hours
snow
    snow.3h Snow volume for the last 3 hours
dt Time of weatherData calculation, unix, UTC
sys
    sys.type Internal parameter
    sys.id Internal parameter
    sys.message Internal parameter
    sys.country Country code (GB, JP etc.)
    sys.sunrise Sunrise time, unix, UTC
    sys.sunset Sunset time, unix, UTC
id City ID
name City name
cod Internal parameter
*/