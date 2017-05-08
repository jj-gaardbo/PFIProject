package com.pfi.jgaard.alarmproject;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Alarm implements Serializable{

    private static final String ALARM_TAG = "ALARM LOG";

    private static final String ALARM_STORAGE_FILE = "alarm_storage.json";

    public String id;

    public String name;

    public int wakeHour;

    public int wakeMinute;

    public boolean active;

    public Alarm(String name, int wakeHour, int wakeMinute, boolean active) {
        this.id = generateID();
        this.name = name;
        this.wakeHour = wakeHour;
        this.wakeMinute = wakeMinute;
        this.active = active;
    }

    private String generateID(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static List<Alarm> getAll(Context context){
        return loadAll(context);
    }

    public static void store(Context context, List<Alarm> alarmList){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(alarmList);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(ALARM_STORAGE_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(ALARM_TAG, "File write failed: " + e.toString());
        }
    }

    public static void save(Context context, Alarm alarm){
        List<Alarm> alarmList = Alarm.loadAll(context);
        alarmList.add(alarm);
        Alarm.store(context, alarmList);
    }

    public static void update(Context context, Alarm alarm){
        List<Alarm> alarmList = Alarm.loadAll(context);
        for (int i = 0; i < alarmList.size(); i++){
            if(alarmList.get(i).getID().equals(alarm.getID())){
                alarmList.set(i, alarm);
            }
        }
        Alarm.store(context, alarmList);
    }

    public static void delete(Context context, Alarm alarm){
        List<Alarm> alarmList = Alarm.loadAll(context);
        for (int i = 0; i < alarmList.size(); i++){
            if(alarmList.get(i).getID().equals(alarm.getID())){
                alarmList.remove(i);
            }
        }
        Alarm.store(context, alarmList);
    }

    public static List<Alarm> loadAll(Context context) {
        String json;
        List<Alarm> alarmList = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput(ALARM_STORAGE_FILE);
            if ( inputStream != null ) {
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, "UTF-8");
                Type listType = new TypeToken<ArrayList<Alarm>>(){}.getType();
                alarmList = new Gson().fromJson(json, listType);
                Log.d(ALARM_TAG, "File Loaded!");
            }
        } catch (FileNotFoundException e) {
            Log.e(ALARM_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(ALARM_TAG, "Can not read file: " + e.toString());
        }
        return alarmList;
    }

    public String getID(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWakeHour() {
        return wakeHour;
    }

    public String getWakeHourString(){
        return String.format(Locale.ENGLISH,"%02d", wakeHour);
    }

    public void setWakeHour(int wakeHour) {
        this.wakeHour = wakeHour;
    }

    public int getWakeMinute() {
        return wakeMinute;
    }

    public String getWakeMinuteString(){
        return String.format(Locale.ENGLISH,"%02d", wakeMinute);
    }

    public void setWakeMinute(int wakeMinute) {
        this.wakeMinute = wakeMinute;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
