package com.lx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] data = {"AAA","BBB","CCC"};
    private Spinner spinner1;
    Spinner spanGrade;
    Spinner spanStu;
    List<String> stuListA;
    List<String> stuListI;
    List<String> graList;
    Map<String,List<String>> map;
    List<String> selectedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 = findViewById(R.id.spinner);
        spanGrade = findViewById(R.id.spinner3);
        spanStu = findViewById(R.id.spinner4);
        fillData();
        //方式1
        spinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                                                        android.R.id.text1,data));
        spanGrade.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                                                        android.R.id.text1,graList));
        spanGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedList.clear();
                selectedList.addAll(map.get(graList.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spanStu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                                                        android.R.id.text1,selectedList));


    }
    public void fillData(){

        graList = new ArrayList<>();
        graList.add("Android");
        graList.add("ios");
        stuListA = new ArrayList<>();
        stuListA.add("小明");
        stuListA.add("小红");
        stuListA.add("小强");
        map = new HashMap<>();
        map.put(graList.get(0),stuListA);
        stuListI = new ArrayList<>();
        stuListI.add("鸣人");
        stuListI.add("佐助");
        stuListI.add("小樱");
        map.put(graList.get(1),stuListI);
    }
    public void toActivity(View view){
        Intent intent = new Intent();
        intent.setAction("xx.ll.test");
        startActivity(intent);
    }
}