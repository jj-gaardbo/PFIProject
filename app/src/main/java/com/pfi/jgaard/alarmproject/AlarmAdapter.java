package com.pfi.jgaard.alarmproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    Context context;
    Switch toggleActive;

    public AlarmAdapter(Context context, List<Alarm> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Alarm alarm = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
        }

        if(alarm != null){
            // Lookup view for data population
            TextView alarmName = (TextView) convertView.findViewById(R.id.alarm_name);
            TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_time);

            // Populate the data into the template view using the data object
            alarmName.setText(alarm.name);
            alarmTime.setText(alarm.getWakeHourString()+":"+alarm.getWakeMinuteString());

            toggleActive = (Switch) convertView.findViewById(R.id.alarm_toggle);
            toggleActive.setEnabled(true);
            toggleActive.setChecked(alarm.active);
        }

        toggleActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarm != null){
                    alarm.setActive(toggleActive.isChecked());
                    Alarm.update(context, alarm);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;

    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

}
