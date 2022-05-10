package com.example.java;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
public class DataAdapter extends ArrayAdapter<DataItem> {
    public DataAdapter(Context context, DataItem[] arr) {
        super(context, R.layout.data_item, arr);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final DataItem dataItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_item, null);
        }

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.begin_data_text)).setText("дата начала: "+dataItem.begin_data);
        ((TextView) convertView.findViewById(R.id.end_data_text)).setText("дата завершения: "+dataItem.end_data);
        ((TextView) convertView.findViewById(R.id.priority_text)).setText(String.valueOf(dataItem.priority));
        return convertView;
    }
}

