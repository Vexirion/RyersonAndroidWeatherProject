package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DBHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "weatherdb.db";

    public DBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //I'm supplying my own DB for this, so don't need to worry about onCreate and onUpdate
    public void onCreate(SQLiteDatabase database){

    }

    public void onUpgrade(SQLiteDatabase database, int oldver, int newver){

    }

}
