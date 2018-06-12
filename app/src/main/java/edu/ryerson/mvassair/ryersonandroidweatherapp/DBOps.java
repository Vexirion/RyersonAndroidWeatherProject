package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.os.AsyncTask;
import java.util.ArrayList;

//it would probably be beneficial to split this into a DBReader and a DBWriter class
//so I can return useful data easier on writes, but I don't really care about that right now.
class DBOps extends AsyncTask <WeatherData, Void, ArrayList> {

    private WeatherDB db;
    private DBOP mode;
    private ArrayList<DBLocation> updatelocs;
    private ArrayList<DBWeatherInfo> weatherupdate;

    public DBOps(WeatherDB db){
        this.db = db;
     }

    protected ArrayList<?> doInBackground(WeatherData... data){
        ArrayList arr = new ArrayList<>();

        switch(mode){
            case LOCREADTRACK:
                arr = db.getLocations(true);
                break;
            case LOCREADALL:
                arr = db.getLocations(false);
                break;
            case DATAREAD:
                arr = db.getSavedWeather();
                break;
            case LOCUPDATE:
                db.updateTrack(updatelocs);
                updatelocs.clear();
                break;
            case DATAUPDATE:
                db.updateWeather(weatherupdate);
                weatherupdate.clear();
                break;
        }
        return arr;
    }

    void setReadMode(DBOP op){
        mode = op;
    }

    void setLocUpdate(ArrayList<DBLocation> locs){
        updatelocs = locs;
        mode = DBOP.LOCUPDATE;
    }

    void setWeatherUpdate(ArrayList<DBWeatherInfo> info){
        weatherupdate = info;
        mode = DBOP.DATAUPDATE;
    }
}
