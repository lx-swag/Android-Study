package com.lx.contentresolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void insert(View view){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.lx.contentprovier.myprovider/student/");
        ContentValues values = new ContentValues();
        values.put("name","jack");
        Uri newUri = resolver.insert(uri, values);
        Toast.makeText(this, "newUri="+newUri.toString(), Toast.LENGTH_SHORT).show();
    }
    public void update(View view){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.lx.contentprovier.myprovider/student/1");
        ContentValues values = new ContentValues();
        values.put("name","lucky");
        resolver.update(uri,values,null,null);
    }
    public void delete(View view){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.lx.contentprovier.myprovider/student/2");
        resolver.delete(uri,null,null);
    }
    public void query(View view){
        Uri uri = Uri.parse("content://com.lx.contentprovier.myprovider/student");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Toast.makeText(this, id+" : "+name, Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}