package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.appointmentmanager.decorator.ReapetedReciever;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity{
    Switch backupSwitch,notificationsSwitch;
    private AlarmManager alarmManager;
    private Intent myIntent;
    private PendingIntent pendingIntent;
   static login log ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        backupSwitch = findViewById(R.id.backupSwitch);


        notificationsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (notificationsSwitch.isChecked()) {
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                myIntent = new Intent(getApplicationContext(), ReapetedReciever.class);
                pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 1, myIntent, 0);
                alarmManager.cancel(pendingIntent);
            }
    else
            {
                log.everyDayReciever();
                log.everyhalfHourReciever();
            }
        });
        backupSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            //TODO
        });
    }



}