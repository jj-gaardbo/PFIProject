package com.pfi.jgaard.alarmproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadCastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = (Alarm) intent.getSerializableExtra("currentAlarm");
        Intent soundAlarm = new Intent(context, SoundAlarm.class);
        soundAlarm.putExtra("currentAlarm", alarm);
        context.startActivity(soundAlarm);
    }

}
