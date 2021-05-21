package com.lx.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText etChinese,etMath,etEnglish;
    private ComputeService.MyBinder binder;
    private TextView avgScore;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (ComputeService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        etChinese = findViewById(R.id.etChinese);
        etEnglish = findViewById(R.id.etEnglish);
        etMath = findViewById(R.id.etMath);
        avgScore = findViewById(R.id.avgScore);
        avgScore.setTextColor(Color.RED);
    }

    public void calcAvg(View view){
        Intent intent = new Intent("heying.computeService");
        intent.setPackage(getPackageName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        double english = Double.parseDouble(etEnglish.getText().toString().trim());
        double math = Double.parseDouble(etMath.getText().toString().trim());
        double chinese = Double.parseDouble(etChinese.getText().toString().trim());
        if (binder!=null) {
            double score = binder.calcScores(chinese, math, english);
            avgScore.setText("您的综合平均分是:"+score);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}