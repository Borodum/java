package com.example.java;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataDataBaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "app";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "items";


    static final String ID = "id";
    static final String HEADING = "heading";
    static final String BEGIN_DATA = "begin_data";
    static final String END_DATA = "end_data";
    static final String PRIORITY = "priority";
    static final String DESCRIPTION = "description";
    static final String CATEGORY = "category";
    static final String VISIBLE = "visible";


    public DataDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
     public void onCreate(SQLiteDatabase db) {
        String request =  "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HEADING + " TEXT NOT NULL, " +
                BEGIN_DATA + " TEXT NOT NULL, " +
                END_DATA + " TEXT NOT NULL, " +
                PRIORITY + " INTEGER NOT NULL, "  +
                DESCRIPTION + " TEXT, " +
                CATEGORY + " TEXT, " +
                VISIBLE + " INTEGER); ";
        db.execSQL(request);

     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

     }
 }
