package com.lx.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SelectedCityListener listener = new SelectedCityListener() {
        @Override
        public void selectCity(String city) {
            Toast.makeText(MainActivity.this,city,Toast.LENGTH_SHORT).show();
            if (isLand){
                fragmentB.setText(city);
            }
        }
    };
    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private FragmentManager manager;
    private boolean isLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_main);
        //屏幕的每次旋转都会重新创建Activity
        Log.e("test", "onCreate: 被创建了" );
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            isLand = false;
        }else {
            isLand = true;
        }
        manager = getSupportFragmentManager();
        fragmentA =(FragmentA) manager.findFragmentById(R.id.fragment1);
        fragmentB = (FragmentB) manager.findFragmentById(R.id.fragment2);
        fragmentA.setOnSelectedCityListener(listener);
//        fragmentB.setText();

    }


}