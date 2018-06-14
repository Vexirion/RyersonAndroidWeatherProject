package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.os.AsyncTask;
import java.util.ArrayList;

//it would probably be beneficial to split this into a DBReader and a DBWriter class
//so I can return useful data easier on writes, but I don't really care about that right now.
class DBOps extends AsyncTask <DBWeatherInfo, Void, ArrayList> {

    private WeatherDB db;
    private DBOP mode;
    private ArrayList<DBLocation> updatelocs;
    private ArrayList<DBWeatherInfo> weatherupdate;

    DBOps(WeatherDB db){
        this.db = db;
    }

    protected ArrayList doInBackground(DBWeatherInfo... data){
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
                break;
            case DATAUPDATE:
                db.updateWeather(weatherupdate);
                break;
        }
        return arr;
    }

    void setReadMode(DBOP op){
        mode = op;
    }

    @SuppressWarnings("unchecked")
    void setLocUpdate(ArrayList<DBLocation> locs){
        updatelocs = (ArrayList<DBLocation>)locs.clone();
        mode = DBOP.LOCUPDATE;
    }

    @SuppressWarnings("unchecked")
    void setWeatherUpdate(ArrayList<DBWeatherInfo> info){
        weatherupdate = (ArrayList<DBWeatherInfo>)info.clone();
        mode = DBOP.DATAUPDATE;
    }
}
