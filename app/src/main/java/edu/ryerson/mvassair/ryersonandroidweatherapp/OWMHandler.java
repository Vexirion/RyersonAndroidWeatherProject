package edu.ryerson.mvassair.ryersonandroidweatherapp;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

class OWMHandler {

    JsonObjectRequest req;
    MainActivity parent;


    OWMHandler(MainActivity parent){
        req = null;
        this.parent = parent;
    }

    protected void makeRequest(String urlsrc, RequestQueue queue){

        System.out.println(urlsrc);

        //Volley JSON Array request. parameters are: HTTP type, URL to access, JSON to send with request, Response listener, Error listener
        req = new JsonObjectRequest(Request.Method.GET, urlsrc, null, new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response){
                if(checkJSON(response))
                    parent.sendData(response);
            }
        }, new Response.ErrorListener(){
            public void onErrorResponse (VolleyError error){
                System.out.println(error.getCause());
            }
        });

        queue.add(req);

    }

    //It looks like Volley already automatically parses all the JSON into dicts and values
    //So we just have to use them as-is. This function currently exists to be a breakpoint for debugger inspection
    private boolean checkJSON(JSONObject JSON){

        if (JSON.has("error")) {
            System.out.println("An HTTP error occurred");
            return false;
        }
        else {
            System.out.println("JSON has already been parsed into JSONObjects and appropriate weatherData types");
            return true;
        }
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