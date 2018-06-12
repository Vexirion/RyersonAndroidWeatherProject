package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class WeatherDB {

    private DBHelper helper;

    public WeatherDB(Context context){
        helper = new DBHelper(context);
    }

    public int updateTrack(DBLocation... locations){
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated = 0;
        for (DBLocation d : locations) {
            ContentValues vals = new ContentValues();
            vals.put(d.KEY_ID, d.id);
            vals.put(d.KEY_NAME, d.name);
            vals.put(d.KEY_COUNTRY, d.country);
            vals.put(d.KEY_SELECTED, d.selected);
            updated += db.update(DBLocation.Table, vals, "?=?", new String[]{d.KEY_ID, Integer.toString(d.id)});
        }
        db.close();
        return updated;
    }

    public int updateWeather(DBWeatherInfo... weather){
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated = 0;
        for (DBWeatherInfo d : weather) {
            ContentValues vals = new ContentValues();
            vals.put(d.KEY_ID, d.id);
            vals.put(d.KEY_TEMPERATURE, d.temperature);
            vals.put(d.KEY_CONDITIONID, d.conditionid);
            vals.put(d.KEY_LASTUPDATE, d.lastupdate.toString());
            updated += db.update(DBWeatherInfo.Table, vals, "?=?", new String[]{d.KEY_ID, Integer.toString(d.id)});
        }
        db.close();
        return updated;
    }

    public ArrayList<DBLocation> getLocations(boolean tracked){
        ArrayList<DBLocation> locations = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String query;

        if (tracked) {
            query = String.format("SELECT * FROM ? WHERE ?=1", new String[]{DBLocation.Table, DBLocation.KEY_SELECTED});
        }
        else {
            query = String.format("SELECT * FROM ?", new String[]{DBLocation.Table});
        }
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(DBLocation.KEY_ID));
                String city = c.getString(c.getColumnIndex(DBLocation.KEY_NAME));
                String country = c.getString(c.getColumnIndex(DBLocation.KEY_COUNTRY));
                int selected = c.getInt(c.getColumnIndex(DBLocation.KEY_SELECTED));
                locations.add(new DBLocation(id, city, country, selected));
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return locations;
    }

    public ArrayList<DBWeatherInfo> getSavedWeather(){
        ArrayList<DBWeatherInfo> weather = new ArrayList<>();

        return weather;
    }

}
