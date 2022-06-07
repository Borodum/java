package com.example.java;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;

public class DataAdapter extends ArrayAdapter<DataItem> /*implements AdapterView.OnItemClickListener*/ {
    DataDataBaseHelper db_helper;
    SQLiteDatabase db;
    Context c;
    public DataAdapter(Context context, ArrayList<DataItem> arr) {
        super(context, R.layout.data_item, arr);
        c=context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final DataItem dataItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_item, null);
        }

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.heading)).setText(Form.firstUpperCase(dataItem.heading));
        ((TextView) convertView.findViewById(R.id.begin_data_text)).setText("дата начала: "+dataItem.begin_data);
        ((TextView) convertView.findViewById(R.id.end_data_text)).setText("дата завершения: "+dataItem.end_data);
        ((TextView) convertView.findViewById(R.id.priority_text)).setText(String.valueOf(dataItem.priority));
        //CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        /*CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        TextView hide = (TextView) convertView.findViewById(R.id.hide);
        //View layout = convertView.findViewById(R.id.list_item);
        if (!checkBox.isChecked()){
        hide.setVisibility(View.INVISIBLE);
        hide.setClickable(false);}

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    hide.setVisibility(View.VISIBLE);
                    hide.setClickable(true);
                  //  layout.setBackgroundColor(0xA1A1A1);
                    //Toast.makeText(getContext(),(checkBox.isChecked()?"true":"false"),Toast.LENGTH_SHORT).show();
                }
                else{
                    hide.setVisibility(View.INVISIBLE);
                    hide.setClickable(false);
                    //layout.setBackgroundColor(0xFFFFFF);
                }

            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),Integer.toString(dataItem.visible), Toast.LENGTH_SHORT).show();
                dataItem.visible = 0;
            }
        });*/

        return convertView;
    }



    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent itemIntent = new Intent(getContext() ,ItemActivity.class);
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

        getContext().startActivity(itemIntent);
    }*/
}

