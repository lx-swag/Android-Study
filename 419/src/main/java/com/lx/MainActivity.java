package com.lx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MSG_FINISH = 0x0001;
    String data = "{\"name\":\"lx\",\"age\":\"3\"}";
    private String[] arr = {"aaa","bbb","ccc","ddd"};
    View mView;
    TextView mSize;
    AlertDialog dialog;
    Button forword;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_FINISH){
                List<Student> list = (List<Student>) msg.obj;
                for (Student student : list) {
                    Log.i("test", "handleMessage: "+student);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.view);
        mSize = findViewById(R.id.textView);
        forword = findViewById(R.id.button4);

        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProgressDialogActivity.class);
                startActivity(intent);
            }
        });

        create();

        JSONObject object = null;
        try {
            object = new JSONObject(data);
            String name = object.getString("name");
            String age = object.getString("age");
            System.out.println("name="+name+",age="+age);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_MOVE:
                        mSize.setText("X="+event.getX()+",Y="+event.getY());
                        break;
                }
                //默认返回为false，表示还没有处理完，向上传递
                return true;
            }
        });
    }
    public void  asyncParser(View view){
        new Thread(){
            @Override
            public void run() {
                try {
                    List<Student> list = parseXml();
                    Message message = handler.obtainMessage();
                    message.what = MSG_FINISH;
                    message.obj = list;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public List<Student> parseXml() throws IOException, XmlPullParserException {
            List<Student> list = null;
            Student student = null;
            XmlPullParser parser = getResources().getXml(R.xml.student);
            int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("student")) {
                            student = new Student();
                        } else if (parser.getName().equals("name")) {
                            student.setName(parser.getAttributeValue(0));
                        } else if (parser.getName().equals("age")) {
                            student.setAge(Integer.parseInt(parser.getAttributeValue(0)));
                        } else if (parser.getName().equals("sex")) {
                            student.setSex(parser.getAttributeValue(0));
                        } else {
                            break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("student")) {
                            list.add(student);
                            Log.i("Test", "parseXml: " + student);
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                eventType = parser.next();
            }
        return list;
        }
    public void singleListAlertDialog(View view){
       new AlertDialog.Builder(this)
               .setTitle("单项列表对话框")
               .setSingleChoiceItems(arr,0, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(MainActivity.this,"您选择了："+arr[which],Toast.LENGTH_LONG).show();
                   }
               })
               .show();
    }
    public void create(){
         dialog = new AlertDialog.Builder(this)
                .setTitle("复选列表对话框")
                .setMultiChoiceItems(arr, new boolean[]{true, false, true, false}, null)
                .create();
    }
    public void mutiListAlertDialog(View view){
        dialog.show();
    }
}