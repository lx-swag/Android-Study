package com.lx.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ComputeService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
   public class MyBinder extends Binder{
       public double calcScores(double... scores){
           int length = scores.length;
           if (length == 0){
               return 0;
           }else {
               int sum = 0;
               for (double score : scores) {
                   sum += score;
               }
               return sum/length;
           }
       }
    }
}
