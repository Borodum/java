package com.example.java;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class AddActivity extends AppCompatActivity {

    DataDataBaseHelper db_helper;
    SQLiteDatabase db;

    CategoryDataBaseHelper cat_helper;
    SQLiteDatabase cat_db;

    public static ArrayList<String> spinnerList;
    Button button;
    String category;
    DataItem dataItem=new DataItem();
    CategoryFragment fragment;
    Spinner spinner;
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = getBaseContext().openOrCreateDatabase(DataDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db_helper = new DataDataBaseHelper(this);
        db_helper.onCreate(db);

        cat_db = getBaseContext().openOrCreateDatabase(CategoryDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        cat_helper = new CategoryDataBaseHelper(this);
        cat_helper.onCreate(cat_db);

        boolean from = getIntent().getBooleanExtra("filled", false);

        make_spinner();
        back_to_cat_db();
        {spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    category = (String) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });}
        button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText head = findViewById(R.id.et1);
                //dataItem.heading = head.getText().toString();
                EditText begin = findViewById(R.id.et2);
                //dataItem.begin_data = begin.getText().toString();
                EditText end = findViewById(R.id.et3);
                //dataItem.end_data = end.getText().toString();
                EditText priority = findViewById(R.id.et4);
                //dataItem.priority = Math.min(20, Integer.parseInt(priority.getText().toString()));
                EditText description = findViewById(R.id.et5);
                //dataItem.description = description.getText().toString();
                if(head.getText().toString().equals("")
                || begin.getText().toString().equals("")
                || end.getText().toString().equals("")
                || priority.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Некоторые обязательные поля не заполнены", Toast.LENGTH_SHORT).show();}
                else{
                    dataItem = Form.make_insert_data(
                            head.getText().toString(),
                            begin.getText().toString(),
                            end.getText().toString(),
                            Math.min(20, Integer.parseInt(priority.getText().toString())),
                            description.getText().toString(), category);

                    if (dataItem == null) {
                        Toast.makeText(AddActivity.this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
                    } else if (Form.data_string_to_int(dataItem.end_data) < Form.data_string_to_int(dataItem.begin_data)) {
                        Toast.makeText(AddActivity.this, "Несоответствие дат \n начало задачи позже срока завершения", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(from){
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("head", dataItem.heading);
                            returnIntent.putExtra("begin", dataItem.begin_data);
                            returnIntent.putExtra("end", dataItem.end_data);
                            returnIntent.putExtra("priority", dataItem.priority);
                            returnIntent.putExtra("describe", dataItem.description);
                            returnIntent.putExtra("category", dataItem.category);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                        else {insert_new_data(dataItem);}
                        finish();
                    }
                }
                //catch (NullPointerException e){Toast.makeText(getApplicationContext(),"Не заполнены обязательные поля",Toast.LENGTH_SHORT).show();}
            }
        });
        Button button_back = findViewById(R.id.back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button catAdd = findViewById(R.id.catAdd);

        catAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Category_fragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment, fragment);
                ft.addToBackStack("tag");
                ft.commit();
            }
        });
        if(from){
            DataItem dataItem = new DataItem();
            dataItem.heading = getIntent().getStringExtra("head");
            dataItem.begin_data = getIntent().getStringExtra("begin");
            dataItem.end_data = getIntent().getStringExtra("end");
            dataItem.priority = getIntent().getIntExtra("priority", 0);
            dataItem.description = getIntent().getStringExtra("describe");
            dataItem.category = getIntent().getStringExtra("category");
            EditText head = findViewById(R.id.et1);
            head.setText(dataItem.heading);
            EditText begin = findViewById(R.id.et2);
            begin.setText(dataItem.begin_data);
            EditText end = findViewById(R.id.et3);
            end.setText(dataItem.end_data);
            EditText priority = findViewById(R.id.et4);
            priority.setText(Integer.toString(dataItem.priority));
            EditText description = findViewById(R.id.et5);
            description.setText(dataItem.description);
            button.setText("Изменить");
            spinner.setSelection(spinnerList.indexOf(dataItem.category));

        }
    }
    void make_spinner(){
        spinnerList  = new ArrayList<>();
        cat_db = cat_helper.getReadableDatabase();
        Cursor cursor = cat_db.rawQuery("SELECT * FROM "+CategoryDataBaseHelper.TABLE_NAME, null);
        //Toast.makeText(getApplicationContext(), Integer.toString(cursor.getCount()),Toast.LENGTH_SHORT).show();
        cursor.moveToFirst();
        for(int i=1;i<cursor.getCount()+1;i++){
            spinnerList.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

    }
    @Override
    protected void onResume(){
        super.onResume();
        cat_db = getBaseContext().openOrCreateDatabase(CategoryDataBaseHelper.DATABASE_NAME, MODE_PRIVATE, null);
        cat_helper = new CategoryDataBaseHelper(this);
        cat_helper.onCreate(db);
        back_to_cat_db();
        make_spinner();
        {spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    category = (String) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });}
    }
    void insert_new_data(DataItem dataItem){
        ContentValues cv = new ContentValues();
        cv.put("heading", dataItem.heading);
        cv.put("begin_data", dataItem.begin_data);
        cv.put("end_data", dataItem.end_data);
        cv.put("priority", dataItem.priority);
        cv.put("description", dataItem.description);
        cv.put("category", dataItem.category);
        cv.put("visible", 1);
        db.insert(DataDataBaseHelper.TABLE_NAME, null, cv);}
    void back_to_cat_db(){
        //Toast.makeText(getApplicationContext(), Integer.toString(spinnerList.size()),Toast.LENGTH_SHORT).show();
        cat_db = cat_helper.getReadableDatabase();
        Cursor cursor = cat_db.rawQuery("SELECT * FROM "+CategoryDataBaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        ArrayList<String> prom = new ArrayList<>();
        for(int i=1;i<cursor.getCount()+1;i++){
            prom.add(cursor.getString(1));cursor.moveToNext();
        }
        cursor.close();
        for(String s: spinnerList){
            if(!prom.contains(s)){
            ContentValues cv = new ContentValues();
            cv.put(CategoryDataBaseHelper.CATEGORY, s);

            cat_db.insert(CategoryDataBaseHelper.TABLE_NAME, null, cv);}
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        back_to_cat_db();

    }
}