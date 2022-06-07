package com.example.java;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Locale;

public class CategoryDataBaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "app";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "categories";

    static final String ID = "id";
    static final String CATEGORY = "category";
    static final String NON_CATEGORY = "без категории";

    public CategoryDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request =  "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY + " TEXT UNIQUE);";
        db.execSQL(request);
        request = "INSERT OR IGNORE INTO "+TABLE_NAME+"("+CATEGORY+") VALUES('"+NON_CATEGORY+"');";
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
