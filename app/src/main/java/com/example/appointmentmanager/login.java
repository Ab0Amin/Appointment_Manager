package com.example.appointmentmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointmentmanager.decorator.ReapetedReciever;
import com.example.appointmentmanager.decorator.getDataReciever;

import java.util.Calendar;

public class login extends AppCompatActivity {

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);


    }


    public void Login(View view) {
        //run recievers
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                everyDayReciever();
                everyhalfHourReciever();

            }
        });
        thread.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void everyDayReciever() {

        Intent in = new Intent(this, getDataReciever.class);
        PendingIntent pe = PendingIntent.getBroadcast(this, 0, in, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pe);

    }

    public void everyhalfHourReciever() {

        Intent in = new Intent(this, ReapetedReciever.class);
        PendingIntent pe = PendingIntent.getBroadcast(this, 0, in, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 5 * 60 * 1000, AlarmManager.INTERVAL_HALF_HOUR, pe);

    }

}