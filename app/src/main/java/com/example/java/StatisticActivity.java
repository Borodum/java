package com.example.java;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class StatisticActivity extends AppCompatActivity {

    StatisticDataBaseHelper statistic_helper;
    SQLiteDatabase statistic_db;

    DataDataBaseHelper db_helper;
    SQLiteDatabase db;

    Button button;

    TextView textView_all, textView_comp, textView_canceled, textView_failed, textView_active;
    int id, all, comp, canceled, failed, active;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

         statistic_db = getBaseContext().openOrCreateDatabase(StatisticDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
         statistic_helper = new StatisticDataBaseHelper(this);
         statistic_helper.onCreate(statistic_db);

        db = getBaseContext().openOrCreateDatabase(DataDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db_helper = new DataDataBaseHelper(this);
        db_helper.onCreate(db);

        button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView_all = findViewById(R.id.text_all);
        textView_comp = findViewById(R.id.text_comp);
        textView_canceled = findViewById(R.id.text_canceled);
        textView_failed = findViewById(R.id.text_failed);
        textView_active = findViewById(R.id.text_active);

        set_stats_points();

        textView_all.setText("Всего: "+ all);
        textView_comp.setText("Выполнено: "+ comp);
        textView_canceled.setText("Отменено: "+ canceled);
        textView_failed.setText("Не выполнено: "+ failed);
        textView_active.setText("Активны: "+ active);

    }
    void set_stats_points(){
        statistic_db = statistic_helper.getReadableDatabase();
        Cursor cursor = statistic_db.rawQuery("SELECT * FROM " + StatisticDataBaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        id = cursor.getInt(0);
        all = cursor.getInt(1);
        comp = cursor.getInt(2);
        canceled = cursor.getInt(3);
        failed = cursor.getInt(4);
        cursor.close();
        db = db_helper.getReadableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + DataDataBaseHelper.TABLE_NAME, null);
        all += cursor2.getCount();
        active = 0;
        cursor2.moveToFirst();
        for(int i=1;i<cursor2.getCount()+1;i++){
            if(cursor2.getInt(7)==0){comp+=1;}
            if(cursor2.getInt(7)==1){active+=1;}
            cursor2.moveToNext();
        }

        cursor2.close();
    }
}
