package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DBHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "weatherdb_test.db";

    //using SQLiteAssetHelper, I don't even have to implement onCreate
    DBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
