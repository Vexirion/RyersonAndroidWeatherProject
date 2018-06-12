package edu.ryerson.mvassair.ryersonandroidweatherapp;

import java.sql.Timestamp;

class WeatherData {

    String location;
    double temperature;
    Timestamp lastupdate;
    WeatherCondition condition;
    int id;

    WeatherData(){
        location = "Placeholder";
        temperature = 25;;
        condition = WeatherCondition.SUN;
        id = 0;
    }

    WeatherData(String loc, int temp, WeatherCondition con, Timestamp time){
        location = loc;
        temperature = temp;
        lastupdate = time;
        condition = con;
    }

}
