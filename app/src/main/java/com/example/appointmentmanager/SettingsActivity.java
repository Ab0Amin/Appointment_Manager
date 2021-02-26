package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity{
    Switch backupSwitch,notificationsSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        backupSwitch = findViewById(R.id.backupSwitch);

        notificationsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            //TODO
        });
        backupSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            //TODO
        });
    }



}