package com.example.java;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatisticDataBaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "app";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "statistics";

    static final String ID = "id";
    static final String AMOUNT = "amount";
    static final String COMPLETED = "completed";
    static final String CANCELED = "canceled";
    static final String FAILED = "failed";

    public StatisticDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request =  "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AMOUNT + " INTEGER, " +
                COMPLETED + " INTEGER, " +
                CANCELED + " INTEGER, " +
                FAILED + " INTEGER);";
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
