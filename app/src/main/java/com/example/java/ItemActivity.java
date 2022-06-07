package com.example.java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;


public class ItemActivity extends Activity {

    DataDataBaseHelper db_helper;
    SQLiteDatabase db;

    StatisticDataBaseHelper statistic_helper;
    SQLiteDatabase statistic_db;

    Button button_back, button_del, button_change, button_comp;
    TextView head, begin, end, priority, describe, category;
    int id;
    DataItem dataItem = new DataItem();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        db = getBaseContext().openOrCreateDatabase(DataDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db_helper = new DataDataBaseHelper(this);
        db_helper.onCreate(db);

        statistic_db = getBaseContext().openOrCreateDatabase(StatisticDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        statistic_helper = new StatisticDataBaseHelper(this);
        statistic_helper.onCreate(statistic_db);

        id = getIntent().getIntExtra("id", 0);
        button_back = findViewById(R.id.back);
        button_change = findViewById(R.id.change_but);
        button_comp = findViewById(R.id.solved_but);
        button_del = findViewById(R.id.del_but);
        head = findViewById(R.id.head);
        begin = findViewById(R.id.begin);
        end = findViewById(R.id.end);
        priority = findViewById(R.id.priority);
        describe = findViewById(R.id.describe);
        category = findViewById(R.id.category);
        head.setText(Form.firstUpperCase(getIntent().getStringExtra("head").toLowerCase()));
        begin.setText("дата начала: "+Form.zero_front(getIntent().getStringExtra("begin")));
        end.setText("дата завершения: "+Form.zero_front(getIntent().getStringExtra("end")));
        priority.setText(Form.priority_to_string(getIntent().getIntExtra("priority", 0)));
        describe.setText(Form.prolong(getIntent().getStringExtra("describe")));
        category.setText("категория: "+getIntent().getStringExtra("category"));
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_invisible(id);
                finish();
            }
        });
        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_data(id);
                //Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(ItemActivity.this, AddActivity.class);
                addIntent.putExtra("filled", true);
                addIntent.putExtra("head", head.getText().toString());
                addIntent.putExtra("begin", getIntent().getStringExtra("begin"));
                addIntent.putExtra("end", getIntent().getStringExtra("end"));
                addIntent.putExtra("priority", getIntent().getIntExtra("priority", 0));
                addIntent.putExtra("describe", describe.getText().toString());
                addIntent.putExtra("category", category.getText().toString());
                startActivityForResult(addIntent, 1);


            }
        });
    }
    void make_invisible(int id){
        ContentValues cv = new ContentValues();
        cv.put(DataDataBaseHelper.VISIBLE, 0);
        db.update(DataDataBaseHelper.TABLE_NAME, cv, DataDataBaseHelper.ID + "=" + id, null);
    }
    void del_data(int id){
        statistic_db = statistic_helper.getReadableDatabase();
        Cursor cursor = statistic_db.rawQuery("SELECT * FROM " + StatisticDataBaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        int id_statistic = cursor.getInt(0);
        int all = cursor.getInt(1);
        int canceled = cursor.getInt(3);
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put(StatisticDataBaseHelper.AMOUNT, all+1);
        cv.put(StatisticDataBaseHelper.CANCELED, canceled+1);
        statistic_db.update(StatisticDataBaseHelper.TABLE_NAME, cv, StatisticDataBaseHelper.ID + "=" + id_statistic, null);
        db.delete(DataDataBaseHelper.TABLE_NAME, DataDataBaseHelper.ID + " = ?",new String[]{String.valueOf(id)});
    }
    void update(DataItem dataItem){
        ContentValues cv = new ContentValues();
        cv.put(DataDataBaseHelper.HEADING, dataItem.heading);
        cv.put(DataDataBaseHelper.BEGIN_DATA, dataItem.begin_data);
        cv.put(DataDataBaseHelper.END_DATA, dataItem.end_data);
        cv.put(DataDataBaseHelper.PRIORITY, dataItem.priority);
        cv.put(DataDataBaseHelper.DESCRIPTION, dataItem.description);
        cv.put(DataDataBaseHelper.CATEGORY, dataItem.category);
        db.update(DataDataBaseHelper.TABLE_NAME, cv, DataDataBaseHelper.ID + "=" + dataItem.id, null);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            dataItem.heading = data.getStringExtra("head");
            //Toast.makeText(getApplicationContext(), dataItem.heading, Toast.LENGTH_SHORT).show();
            dataItem.begin_data = data.getStringExtra("begin");
            dataItem.end_data = data.getStringExtra("end");
            dataItem.priority = data.getIntExtra("priority", 0);
            dataItem.description = data.getStringExtra("describe");
            dataItem.category = data.getStringExtra("category");
            head.setText(Form.firstUpperCase(dataItem.heading));
            begin.setText("дата начала: "+Form.zero_front(dataItem.begin_data));
            end.setText("дата завершения: "+Form.zero_front(dataItem.end_data));
            priority.setText(Form.priority_to_string(dataItem.priority));
            describe.setText(Form.prolong(dataItem.description));
            category.setText("категория: "+dataItem.category);
            dataItem.id = id;
            update(dataItem);
        }
    }
    /*@Override
    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent r) {
            dataItem.heading = r.getStringExtra("head");
            Toast.makeText(getApplicationContext(), dataItem.heading, Toast.LENGTH_SHORT).show();
            dataItem.begin_data = r.getStringExtra("begin");
            dataItem.end_data = r.getStringExtra("end");
            dataItem.priority = r.getIntExtra("priority", 0);
            dataItem.description = r.getStringExtra("describe");
            dataItem.category = r.getStringExtra("category");
            head.setText(Form.firstUpperCase(dataItem.heading));
            begin.setText("дата начала: "+Form.zero_front(dataItem.begin_data));
            end.setText("дата завершения: "+Form.zero_front(dataItem.end_data));
            priority.setText(Form.priority_to_string(dataItem.priority));
            describe.setText(Form.prolong(dataItem.description));
            category.setText("категория: "+dataItem.category);
            dataItem.id = id;
            update(dataItem);}*/
}