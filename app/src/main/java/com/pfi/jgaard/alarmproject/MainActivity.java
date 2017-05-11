package com.pfi.jgaard.alarmproject;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    Sensor gyro = null;
    boolean hasGyro = false;

    List<Alarm> alarms;
    ArrayList<PendingIntent> pendingIntents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAlarmList();
        setupAlarms();

        //Sensor setup
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(gyro != null) hasGyro = true;
    }

    public void cancelPending(){
        for (int i = 0; i < pendingIntents.size(); i++){
            pendingIntents.get(i).cancel();
        }
    }

    public void setupAlarms(){
        cancelPending();
        if(alarms.size() > 0){

            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            for(int i = 0; i < alarms.size(); i++)
            {
                if(!alarms.get(i).isActive()){
                    continue;
                }
                Calendar cal = Calendar.getInstance();

                long sysTime = System.currentTimeMillis();
                cal.set(Calendar.HOUR_OF_DAY, alarms.get(i).getWakeHour());
                cal.set(Calendar.MINUTE, alarms.get(i).getWakeMinute());
                cal.set(Calendar.SECOND, 0);
                long calTimeMs = cal.getTimeInMillis();

                if(sysTime > calTimeMs){continue;}

                Intent alarmIntent = new Intent(this, AlarmBroadCastReciever.class);
                alarmIntent.putExtra("currentAlarm", alarms.get(i));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                final int SDK_INT = Build.VERSION.SDK_INT;
                if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                    am.set(AlarmManager.RTC_WAKEUP, calTimeMs, pendingIntent);
                }
                else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
                    am.setExact(AlarmManager.RTC_WAKEUP, calTimeMs, pendingIntent);
                }
                else if (SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calTimeMs, pendingIntent);
                }
                pendingIntents.add(pendingIntent);
            }
        }

    }

    public void setupAlarmList(){
        // Construct the data source
        List<Alarm> arrayOfAlarms = Alarm.getAll(this);
        this.alarms = arrayOfAlarms;
        // Create the adapter to convert the array to views
        AlarmAdapter adapter = new AlarmAdapter(this, arrayOfAlarms);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.alarm_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Intent editAlarm = new Intent(getApplicationContext(), AlarmEdit.class);
                Alarm eAlarm = (Alarm) list.getItemAtPosition(pos);
                editAlarm.putExtra("editAlarm", eAlarm);
                startActivity(editAlarm);
            }
        });
    }

    public void alarm_add(View view){
        startActivity(new Intent(getApplicationContext(), AlarmEdit.class));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float val = event.values[1];
        TextView shit = (TextView)findViewById(R.id.values);
        shit.setText(val+"");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
