package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataAdapter adapter = new DataAdapter(this, makeData());
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);
    }

    // Метод cоздания массива дат
    DataItem[] makeData() {
        DataItem[] arr = new DataItem[10];

// даты начала
        String[] beginArr = {"01.01.1789", "22.06.1941", "07.07.2007", "10.05.2022", "07.10.2390", "22.09.2654", "", "13.05.2998", "02.06.3011", "23.12.3289"};
// даты конца
        String[] endArr = {"11.01.1789", "02.07.1941", "17.07.2007", "20.05.2022", "17.10.2390", "02.10.2654", "", "23.05.2998", "12.06.3011", "02.01.3290"};
// приоритет задачи
        int[] priorityArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

// Сборка дат
        for (int i = 0; i < arr.length; i++) {
            DataItem dataItem = new DataItem();
            dataItem.begin_data = beginArr[i];
            dataItem.end_data = endArr[i];
            dataItem.priority = priorityArr[i];
            arr[i] = dataItem;
        }
        return arr;
    }
}

