package com.lx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class DynamicGridViewActivity extends AppCompatActivity implements View.OnClickListener{
    EditText text;
    Button button8;
    GridView gridView;
    ArrayAdapter adapter;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_grid_view);
        initView();
        list = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,android.R.id.text1,list);
        gridView.setAdapter(adapter);
        button8.setOnClickListener(this);
    }
    public void initView(){
        text = findViewById(R.id.editTextTextPersonName);
        button8 = findViewById(R.id.button8);
        gridView = findViewById(R.id.gridView2);
    }

    @Override
    public void onClick(View v) {
        String name = text.getText().toString().trim();
        list.add(name);
        adapter.notifyDataSetChanged();
    }
}