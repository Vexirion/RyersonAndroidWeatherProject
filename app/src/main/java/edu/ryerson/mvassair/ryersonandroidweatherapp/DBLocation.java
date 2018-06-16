package edu.ryerson.mvassair.ryersonandroidweatherapp;

class DBLocation {

    public static final String Table = "CityIDs";
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COUNTRY = "COUNTRY";
    public static final String KEY_SELECTED = "SELECTED";

    public int id;
    public String name;
    public String country;
    public int selected;
    DBWeatherInfo weather;

    public DBLocation(int id, String name, String country, int selected){
        this.id = id;
        this.name = name;
        this.country = country;
        this.selected = selected;

    }

}
