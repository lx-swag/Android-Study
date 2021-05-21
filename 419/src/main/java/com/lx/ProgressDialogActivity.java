package com.lx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

public class ProgressDialogActivity extends AppCompatActivity {
    private static final int DOWNLOAD_FINISH = 0x0002;
    private static final int DOWNLOAD_PROGRESS = 0x0001;
    private ProgressDialog progressDialog;
    private Button mViewGroup;
    private Button button7;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case DOWNLOAD_PROGRESS:
                    progressDialog.setProgress(msg.arg1);
                    break;
                case  DOWNLOAD_FINISH:
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);
        mViewGroup = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressDialogActivity.this,DynamicGridViewActivity.class);
                startActivity(intent);
            }
        });
        mViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressDialogActivity.this, GridViewActivity.class);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_launcher_foreground);
        progressDialog.setTitle("进度条对话框");
        progressDialog.setMessage("当前下载进度");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
    public void download(View view){
        progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    Message message = handler.obtainMessage();
                    message.arg1 = i;
                    message.what = DOWNLOAD_PROGRESS;
                    handler.sendMessage(message);
                    try {
                       TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(DOWNLOAD_FINISH);
            }
        }.start();
    }
}