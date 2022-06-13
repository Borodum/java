package com.example.java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteUselessCategoryFragment extends Fragment {
    String category;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_useless_category, container, false);
        ArrayList<String> spinnerList = AddActivity.spinnerDelList;
        Button button = view.findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });
        {spinner = view.findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if(spinner==null){
                Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
            }
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
        Button del_but = view.findViewById(R.id.del_button);
        del_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner != null && spinner.getSelectedItem() !=null ) {
                    String s = spinner.getSelectedItem().toString();
                AddActivity.spinnerDelList.remove(s);
                AddActivity.spinnerList.remove(s);
                AddActivity.del_per_session.add(s);
                requireActivity().getSupportFragmentManager().popBackStack();}
                else{
                    Toast.makeText(getContext(), "Ничего не выбранно", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}