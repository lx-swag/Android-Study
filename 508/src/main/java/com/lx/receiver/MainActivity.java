package com.lx.receiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MyReceiver2 receiver2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("two");
        registerReceiver(receiver2, filter);
        button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SendSmsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver2);
    }

    public void send(View view){
        Intent intent = new Intent();
        switch(view.getId()){
            case R.id.button:
                intent = new Intent("heying.receiver");
                // 下面这一行在Android 7.0及以下版本不是必须的，但是Android 8.0或者更高版本，发送广播的条件更加严苛，必须添加这一行内容。
// 创建的ComponentName实例化对象有两个参数，第1个参数是指接收广播类的包名，第2个参数是指接收广播类的完整类名。
                intent.setComponent(new ComponentName(getPackageName(),"com.lx.receiver.MyReceiver"));
                sendBroadcast(intent);
                break;
            case R.id.button2:
                intent = new Intent("two");
                intent.setComponent(new ComponentName(getPackageName(),"com.lx.receiver.MyReceiver2"));
                sendBroadcast(intent);
                break;
        }
    }
}