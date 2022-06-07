package com.example.java;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.TreeMap;

public class Form {

    static TreeMap<Integer, String> pr_map=new TreeMap<Integer, String>(){{
        put(0, "снят");
        put(1,"очень низкий");
        put(2,"очень низкий");
        put(3,"низкий");
        put(4,"низкий");
        put(5,"низкий");
        put(6,"ниже среднего");
        put(7,"ниже среднего");
        put(8,"средний");
        put(9,"средний");
        put(10,"средний");
        put(11,"средний");
        put(12,"выше среднего");
        put(13,"выше среднего");
        put(14,"высокий");
        put(15,"высокий");
        put(16,"высокий");
        put(17,"очень высокий");
        put(18,"очень высокий");
        put(19,"очень высокий");
        put(20,"наивысший");
    }};

    static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    static String priority_to_string(int pr){
        return "приоритет: "+pr_map.get(pr)+"(" + pr+ ")";
    }

    static String prolong(String string){
        int n = 1000 - string.length();
        StringBuilder stringBuilder = new StringBuilder(string);
        for(; n>0; n--){
            stringBuilder.append(" ");}
        string = stringBuilder.toString();
        return string;
    }

    @SuppressLint("DefaultLocale")
    static String zero_front(String string){
        String[] arr = string.split("\\.");
        return String.format("%02d", Integer.parseInt(arr[0])) + "."
                + String.format("%02d", Integer.parseInt(arr[1])) + "."
                + String.format("%04d", Integer.parseInt(arr[2]));
    }

    static boolean check_correct_data(int data_val){
        int y=data_val/10000, m = data_val%10000/100, d=data_val%100;
        switch (m) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if(d<=31 && d>0){return true;}
            case 4:
            case 6:
            case 9:
            case 11:
                if(d<=30 && d>0)return true;
            case 2:
                int ves=0;
                if(y%400==0||y%4==0&&y%100!=0){ves=1;}
                if(d<=28+ves && d>0)return true;
            default:
                return false;
        }
    }

    static int data_string_to_int(String data){
        try {
            String[] arr = data.split("\\.");
            return  Integer.parseInt(arr[0]) + Integer.parseInt(arr[1]) * 100 + Integer.parseInt(arr[2]) * 10000;
        }
        catch (IndexOutOfBoundsException e){
            return 0;
        }
    }

    static String data_int_to_string(int data){
        int n1=data%100, n2=data%10000/100, n3=data/10000;
        return Integer.toString(n1)+"."+Integer.toString(n2)+"."+Integer.toString(n3);
    }

    static DataItem make_insert_data(String head, String begin, String end, int pr, String desc, String category){
        DataItem dataItem = new DataItem();
        dataItem.heading = firstUpperCase(head);
        dataItem.begin_data = begin;
        if(!check_correct_data(data_string_to_int(dataItem.begin_data)))
            return null;
        dataItem.end_data = end;
        if(!check_correct_data(data_string_to_int(dataItem.end_data)))
            return null;
        dataItem.priority = pr;
        dataItem.description = desc;
        dataItem.category = category;
        return dataItem;
    }

    static String to_correct_form(int a){
        String s="", e;
        int d = a%100;
        switch (d){
            case 11:
            case 12:
            case 13:
            case 14:
                d = 5;
                break;
            default:
                d = a%10;
        }
        switch (d){
            case 1:
                e = "е";
                break;
            case 2:
            case 3:
            case 4:
                e = "я";
                break;
            default:
                e = "й";

        }
        s = "Завершено по времени: " + Integer.toString(a) + " задани" + e;
        return s;
    }

}