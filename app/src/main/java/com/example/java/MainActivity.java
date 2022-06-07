package com.example.java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity {

    DataDataBaseHelper db_helper;
    SQLiteDatabase db;

    StatisticDataBaseHelper statistic_helper;
    SQLiteDatabase statistic_db;

    ImageButton addButton;
    Button statsButton;
    //String c = "0";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = getBaseContext().openOrCreateDatabase(DataDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db_helper = new DataDataBaseHelper(this);
        db_helper.onCreate(db);

        statistic_db = getBaseContext().openOrCreateDatabase(StatisticDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        statistic_helper = new StatisticDataBaseHelper(this);
        statistic_helper.onCreate(statistic_db);
        //insert_new_data();
        DataAdapter adapter = new DataAdapter(MainActivity.this, makeData());
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemIntent = new Intent(MainActivity.this, ItemActivity.class);
                String head = ((DataItem) parent.getItemAtPosition(position)).heading;
                itemIntent.putExtra("head", head);
                String begin = ((DataItem) parent.getItemAtPosition(position)).begin_data;
                itemIntent.putExtra("begin", begin);
                String end = ((DataItem) parent.getItemAtPosition(position)).end_data;
                itemIntent.putExtra("end", end);
                int priority = ((DataItem) parent.getItemAtPosition(position)).priority;
                itemIntent.putExtra("priority", priority);
                String describe = ((DataItem) parent.getItemAtPosition(position)).description;
                itemIntent.putExtra("describe", describe);

                startActivity(itemIntent);
            }
        });
        addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                addIntent.putExtra("filled", false);
                startActivity(addIntent);

            }
        });
        statsButton = findViewById(R.id.statistic);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
        set_statistic_if_null();
        clean_gone_data();

    }
    @Override
    public void onResume(){
        super.onResume();
        db = getBaseContext().openOrCreateDatabase(DataDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db_helper = new DataDataBaseHelper(this);
        db_helper.onCreate(db);
        DataAdapter adapter = new DataAdapter(MainActivity.this, makeData());
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemIntent = new Intent(MainActivity.this, ItemActivity.class);
                int DIid = ((DataItem) parent.getItemAtPosition(position)).id;
                itemIntent.putExtra("id", DIid);
                String head = ((DataItem) parent.getItemAtPosition(position)).heading;
                itemIntent.putExtra("head", head);
                String begin = ((DataItem) parent.getItemAtPosition(position)).begin_data;
                itemIntent.putExtra("begin", begin);
                String end = ((DataItem) parent.getItemAtPosition(position)).end_data;
                itemIntent.putExtra("end", end);
                int priority = ((DataItem) parent.getItemAtPosition(position)).priority;
                itemIntent.putExtra("priority", priority);
                String describe = ((DataItem) parent.getItemAtPosition(position)).description;
                itemIntent.putExtra("describe", describe);
                String category = ((DataItem) parent.getItemAtPosition(position)).category;
                itemIntent.putExtra("category", category);
                startActivity(itemIntent);
            }
        });
        //updateData(arrayList);
        //c += "1";
    }

    // Метод cоздания массива дат
    ArrayList<DataItem> makeData() {
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataDataBaseHelper.TABLE_NAME +" WHERE visible=1", null);
        String[] rows = cursor.getColumnNames();
        ArrayList<DataItem> arr = new ArrayList<>();
        cursor.moveToFirst();
        for(int i=1;i<cursor.getCount()+1;i++){
            DataItem dataItem = new DataItem();
            dataItem.id = cursor.getInt(0);
            //Toast.makeText(getApplicationContext(), Integer.toString(dataItem.id), Toast.LENGTH_SHORT).show();
            dataItem.heading = Form.firstUpperCase(cursor.getString(1));
            dataItem.begin_data = Form.zero_front(cursor.getString(2));
            dataItem.end_data = Form.zero_front(cursor.getString(3));
            dataItem.priority = cursor.getInt(4);
            dataItem.description = cursor.getString(5);
            dataItem.category = cursor.getString(6);
            dataItem.visible = cursor.getInt(7);
            //Toast.makeText(getApplicationContext(),dataItem.visible+"!",Toast.LENGTH_LONG).show();
            arr.add(dataItem);
            cursor.moveToNext();
        }
        cursor.close();


        return arr;
    }
    void set_statistic_if_null(){
        statistic_db = statistic_helper.getReadableDatabase();
        Cursor cursor = statistic_db.rawQuery("SELECT * FROM " + StatisticDataBaseHelper.TABLE_NAME, null);
        if (cursor.getCount()==0){
            ContentValues cv = new ContentValues();
            cv.put(StatisticDataBaseHelper.AMOUNT, 0);
            cv.put(StatisticDataBaseHelper.COMPLETED, 0);
            cv.put(StatisticDataBaseHelper.CANCELED, 0);
            cv.put(StatisticDataBaseHelper.FAILED, 0);
            statistic_db.insert(StatisticDataBaseHelper.TABLE_NAME, null, cv);
        }
        cursor.close();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void clean_gone_data(){
        statistic_db = statistic_helper.getReadableDatabase();
        Cursor cursor = statistic_db.rawQuery("SELECT * FROM " + StatisticDataBaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        String[] ymd = java.time.LocalDateTime.now().toString().substring(0, 10).split("-");
        int now = Form.data_string_to_int(ymd[2]+"."+ymd[1]+"."+ymd[0]);
        int id = cursor.getInt(0);
        int all = cursor.getInt(1);
        int comp = cursor.getInt(2);
        int canceled = cursor.getInt(3);
        int failed = cursor.getInt(4);
        db = db_helper.getReadableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + DataDataBaseHelper.TABLE_NAME, null);
        cursor2.moveToFirst();
        int c=0;
        for(int i=1;i<cursor2.getCount()+1;i++){
            if (now>Form.data_string_to_int(cursor2.getString(3))){
                if (cursor2.getInt(7)==1){failed+=1;c++;}
                if (cursor2.getInt(7)==0){comp+=1;}
                del_data(cursor2.getInt(0));
            }
            cursor2.moveToNext();
        }
        all = canceled + comp + failed;
        if(c!=0){Toast.makeText(getApplicationContext(), Form.to_correct_form(c), Toast.LENGTH_LONG).show();}
        cursor.close();cursor2.close();
        ContentValues cv = new ContentValues();
        cv.put(StatisticDataBaseHelper.AMOUNT, all);
        cv.put(StatisticDataBaseHelper.COMPLETED, comp);
        cv.put(StatisticDataBaseHelper.CANCELED, canceled);
        cv.put(StatisticDataBaseHelper.FAILED, failed);
        statistic_db.update(StatisticDataBaseHelper.TABLE_NAME, cv, StatisticDataBaseHelper.ID + "=" + id, null);
    }
    void del_data(int id){
        db.delete(DataDataBaseHelper.TABLE_NAME, DataDataBaseHelper.ID + " = ?",new String[]{String.valueOf(id)});
    }


    /*public void improve(int id, String s) {
        ContentValues cv = new ContentValues();
        cv.put(s, 0);
        db.update(DataDataBaseHelper.TABLE_NAME, cv, DataDataBaseHelper.ID + "=" + id, null);
    }*/
    /*void updateData(ArrayList<DataItem> arr){
        db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataDataBaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        String[] rows = cursor.getColumnNames();
        //Toast.makeText(getApplicationContext(), Arrays.toString(rows), Toast.LENGTH_LONG).show();
        for(int i=0;i<cursor.getCount();i++){
            ContentValues cv = new ContentValues();
            cv.put(rows[1], arr.get(i).heading);
            cv.put(rows[2], arr.get(i).begin_data);
            cv.put(rows[3], arr.get(i).end_data);
            cv.put(rows[4], arr.get(i).priority);
            cv.put(rows[5], arr.get(i).description);
            cv.put(rows[6], arr.get(i).category);
            cv.put(rows[7], arr.get(i).visible);

            db.update(DataDataBaseHelper.TABLE_NAME, cv, DataDataBaseHelper.ID + "=" + arr.get(i).id, null);
            cursor.moveToNext();
        }
        cursor.close();
    }*/
}


