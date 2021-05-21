package com.lx.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private MyFragment myFragment ;
    private FragmentManager manager;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager  = getSupportFragmentManager();
//        myFragment = (MyFragment) manager.findFragmentById(R.id.fragment);
//        myFragment.setText("哈哈哈哈哈");
    }
    public void addFragment(View view){
//        manager = getSupportFragmentManager();
        myFragment = new MyFragment();
        manager.beginTransaction()
                .add(R.id.layout,myFragment,"test")
                .commit();
    }
    public void removeFragment(View view){
        Fragment fragment = manager.findFragmentByTag("test");
        manager.beginTransaction()
                .remove(fragment)
                .commit();
    }
    public void hideAndShowFragment(View view){
        Fragment fragment = manager.findFragmentByTag("test");
        if (fragment.isHidden()){
            manager.beginTransaction().show(fragment).commit();
        }else {
            manager.beginTransaction().hide(fragment).commit();
        }
    }
    public void replaceFragment(View view){
        count++;
        MyFragment myFragment = MyFragment.newInstance(count);
        manager.beginTransaction()
                .replace(R.id.layout,myFragment)
                .addToBackStack(null)
                .commit();
    }


}