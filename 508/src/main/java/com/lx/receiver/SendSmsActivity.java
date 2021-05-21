package com.lx.receiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSmsActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private EditText content;
    private Button send;
    private SendSms sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS},  1);
         sms = new SendSms();
        IntentFilter filter = new IntentFilter("heying.sendSms");
        registerReceiver(sms,filter);
        init();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNumber.getText().toString().trim();
                String text = content.getText().toString().trim();
                Intent intent = new Intent("heying.sendSms");
                intent.setComponent(new ComponentName(getPackageName(),getPackageName()+".SendSms"));
                PendingIntent sendIntent = PendingIntent.getBroadcast(SendSmsActivity.this, 0, intent, 0);
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(phone,"",text,sendIntent,null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sms);
    }

    private void init(){
        phoneNumber = findViewById(R.id.editTextTextPersonName);
        content = findViewById(R.id.editTextTextPersonName2);
        send = findViewById(R.id.button4);
    }

}