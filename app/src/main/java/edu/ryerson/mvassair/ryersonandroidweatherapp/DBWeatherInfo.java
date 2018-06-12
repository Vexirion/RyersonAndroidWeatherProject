package edu.ryerson.mvassair.ryersonandroidweatherapp;

import java.sql.Timestamp;

class DBWeatherInfo {

    public static final String Table = "WeatherInfo";
    public static final String KEY_ID = "ID";
    public static final String KEY_TEMPERATURE = "TEMPERATURE";
    public static final String KEY_CONDITIONID = "CONDITIONID";
    public static final String KEY_LASTUPDATE = "LASTUPDATE";

    public int id;
    public int temperature;
    public int conditionid;
    public Timestamp lastupdate;


    public DBWeatherInfo(){
        id = 0;
        temperature = 0;
        conditionid = 0;
        lastupdate.setTime(0);
    }

    public DBWeatherInfo(int id, int temperature, int conditionid, String time){
        this.id = id;
        this.temperature = temperature;
        this.conditionid = conditionid;
        lastupdate = Timestamp.valueOf(time);
    }

}
