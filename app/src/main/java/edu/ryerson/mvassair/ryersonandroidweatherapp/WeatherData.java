package edu.ryerson.mvassair.ryersonandroidweatherapp;

class WeatherData {

    String location;
    int temperature;
    boolean celsius;
    WeatherCondition condition;

    WeatherData(){
        location = "Placeholder";
        temperature = 25;
        celsius = true;
        condition = WeatherCondition.SUN;
    }

    WeatherData(String loc, int temp, boolean cel, WeatherCondition con){
        location = loc;
        temperature = temp;
        celsius = cel;
        condition = con;
    }

}
