package com.pfi.jgaard.alarmproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlarmService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(intent.getStringExtra("currentAlarm"), Alarm.class);
        Intent soundAlarm = new Intent(getApplicationContext(), SoundAlarm.class);
        soundAlarm.putExtra("currentAlarm", alarm);
        getApplicationContext().startActivity(soundAlarm);
        return START_NOT_STICKY;
    }

}
