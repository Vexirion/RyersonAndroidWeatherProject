package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;

class WeatherDB {

    private DBHelper helper;

    WeatherDB(Context context){
        helper = new DBHelper(context);
    }

    public int updateTrack(ArrayList<DBLocation> locations){
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated = 0;
        for (DBLocation d : locations) {
            ContentValues vals = new ContentValues();
            vals.put(DBLocation.KEY_ID, d.id);
            vals.put(DBLocation.KEY_NAME, d.name);
            vals.put(DBLocation.KEY_COUNTRY, d.country);
            vals.put(DBLocation.KEY_SELECTED, d.selected);
            updated += db.update(DBLocation.Table, vals, "?=?", new String[]{DBLocation.KEY_ID, Integer.toString(d.id)});
        }
        db.close();
        return updated;
    }

    public int updateWeather(ArrayList<DBWeatherInfo> weather){
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated = 0;
        for (DBWeatherInfo d : weather) {
            ContentValues vals = new ContentValues();
            vals.put(DBWeatherInfo.KEY_ID, d.id);
            vals.put(DBWeatherInfo.KEY_TEMPERATURE, d.temperature);
            vals.put(DBWeatherInfo.KEY_CONDITIONID, d.conditionid);
            vals.put(DBWeatherInfo.KEY_LASTUPDATE, d.lastupdate.toString());
            vals.put(DBWeatherInfo.KEY_CATEGORY, d.category.ordinal());
            vals.put(DBWeatherInfo.KEY_LOCATION, d.location);
            updated += db.update(DBWeatherInfo.Table, vals, "?=?", new String[]{DBWeatherInfo.KEY_ID, Integer.toString(d.id)});
        }
        db.close();
        return updated;
    }

    public ArrayList<DBLocation> getLocations(boolean tracked){
        ArrayList<DBLocation> locations = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String query;

        if (tracked) {
            query = String.format("SELECT * FROM %s WHERE %s=1", DBLocation.Table, DBLocation.KEY_SELECTED);
        }
        else {
            query = String.format("SELECT * FROM %s", DBLocation.Table);
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
            c.close();
        }
        db.close();
        return locations;
    }

    public ArrayList<DBWeatherInfo> getSavedWeather(){
        ArrayList<DBWeatherInfo> weather = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();


        String query = String.format("Select * from %s", DBWeatherInfo.Table);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(DBWeatherInfo.KEY_ID));
                double temp = c.getDouble(c.getColumnIndex(DBWeatherInfo.KEY_TEMPERATURE));
                int condid = c.getInt(c.getColumnIndex(DBWeatherInfo.KEY_CONDITIONID));
                WeatherCondition category = WeatherCondition.values()[c.getInt(c.getColumnIndex(DBWeatherInfo.KEY_CATEGORY))];
                Timestamp lastupdate = Timestamp.valueOf(c.getString(c.getColumnIndex(DBWeatherInfo.KEY_LASTUPDATE)));
                String location = c.getString(c.getColumnIndex(DBWeatherInfo.KEY_LOCATION));

                weather.add(new DBWeatherInfo(id, temp, condid, category, lastupdate, location));
            }while (c.moveToNext());
            c.close();
        }
        db.close();
        return weather;
    }
}
