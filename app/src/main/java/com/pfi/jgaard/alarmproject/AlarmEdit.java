package com.pfi.jgaard.alarmproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class AlarmEdit extends AppCompatActivity {

    private static final int DEFAULT_HOUR = 7;
    private static final int DEFAULT_MINUTE = 0;

    Alarm eAlarm;

    EditText name;
    TimePicker timePicker;
    Button deleteButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eAlarm = (Alarm) extras.getSerializable("editAlarm");
        }

        //Show delete button if alarm is being edited
        name = (EditText) findViewById(R.id.alarm_name);
        deleteButton = (Button) findViewById(R.id.alarm_delete);
        saveButton = (Button) findViewById(R.id.alarm_save);
        timePicker = (TimePicker) findViewById(R.id.alarm_timepicker);
        timePicker.setIs24HourView(true);

        if(eAlarm != null){
            name.setText(eAlarm.getName());
            deleteButton.setVisibility(View.VISIBLE);
            timePicker.setCurrentHour(eAlarm.getWakeHour());
            timePicker.setCurrentMinute(eAlarm.getWakeMinute());
        } else {
            name.setText(R.string.alarm_default_name);
            deleteButton.setVisibility(View.GONE);
            timePicker.setCurrentHour(DEFAULT_HOUR);
            timePicker.setCurrentMinute(DEFAULT_MINUTE);
        }

    }

    public void delete_alarm(View view){
        Alarm.delete(getApplicationContext(), eAlarm);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void save_alarm(View view){
        if(eAlarm != null){
            eAlarm.setName(name.getText().toString());
            eAlarm.setWakeHour(timePicker.getCurrentHour());
            eAlarm.setWakeMinute(timePicker.getCurrentMinute());
            Alarm.update(getApplicationContext(), eAlarm);
        } else {
            Alarm.save(getApplicationContext(), new Alarm(name.getText().toString(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), true));
        }
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
