package edu.ryerson.mvassair.ryersonandroidweatherapp;

import java.sql.Timestamp;

class DBWeatherInfo {

    public static final String Table = "WeatherInfo";
    public static final String KEY_ID = "ID";
    public static final String KEY_TEMPERATURE = "TEMPERATURE";
    public static final String KEY_CONDITIONID = "CONDITIONID";
    public static final String KEY_CATEGORY = "CATEGORY";
    public static final String KEY_LASTUPDATE = "LASTUPDATE";
    public static final String KEY_LOCATION = "LOCATION";


    public int id;
    public double temperature;
    public int conditionid;
    public WeatherCondition category;
    public Timestamp lastupdate;
    public String location;
    public  String country = "";


    public DBWeatherInfo(){
        id = 0;
        temperature = 0;
        conditionid = 0;
        category = WeatherCondition.OTHER;
        lastupdate.setTime(0);
        location = "Placeholder";
    }

    DBWeatherInfo(int id, double temperature, int conditionid, WeatherCondition category, Timestamp time, String loc){
        this.id = id;
        this.temperature = temperature;
        this.conditionid = conditionid;
        this.category = category;
        lastupdate = time;
        location = loc;
    }

}
