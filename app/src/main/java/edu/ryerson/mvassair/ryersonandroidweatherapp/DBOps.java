package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.os.AsyncTask;
import android.util.SparseArray;

import java.net.SocketAddress;
import java.util.ArrayList;

//it would probably be beneficial to split this into a DBReader and a DBWriter class
//so I can return useful data easier on writes, but I don't really care about that right now.
class DBOps extends AsyncTask <Integer, Void, SparseArray> {

    private WeatherDB db;
    private DBOP mode;
    private SparseArray<DBLocation> updatelocs;
    private SparseArray<DBWeatherInfo> weatherupdate;

    DBOps(WeatherDB db){
        this.db = db;
    }

    protected SparseArray doInBackground(Integer... ids){
        SparseArray arr = new SparseArray<>();

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
                db.updateTrack(ids);
                break;
            case DATAUPDATE:
                db.updateWeather(ids);
                break;
        }
        return arr;
    }

    void setReadMode(DBOP op){
        mode = op;
    }

    @SuppressWarnings("unchecked")
    void setLocUpdate(SparseArray<DBLocation> locs){
        updatelocs = locs.clone();
        mode = DBOP.LOCUPDATE;
    }

    @SuppressWarnings("unchecked")
    void setWeatherUpdate(SparseArray<DBWeatherInfo> info){
        weatherupdate = info.clone();
        mode = DBOP.DATAUPDATE;
    }
}
