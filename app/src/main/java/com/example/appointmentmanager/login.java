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
    MyPrefernce Prefrence;
    String woirking;
    Intent in24,in5;
    PendingIntent pe24 , pe5;
    AlarmManager alarmManager24,alarmManager5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        SettingsActivity.log = this;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
         Prefrence = MyPrefernce.getInstance(this);
        woirking = Prefrence.getStringData("receiver");
        if (!woirking.equals("true")) {
            //run recievers
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    everyDayReciever();
                    everyhalfHourReciever();
                    Prefrence.storeData("receiver","true");

                }
            });
            thread.start();
        }


    }


    public void Login(View view) {


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void everyDayReciever() {

         in24 = new Intent(this, getDataReciever.class);
         pe24 = PendingIntent.getBroadcast(this, 0, in24, 0);
         alarmManager24 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager24.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pe24);

    }

    public void everyhalfHourReciever() {

         in5 = new Intent(this, ReapetedReciever.class);
         pe5 = PendingIntent.getBroadcast(this, 0, in5, 0);
         alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager5.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 5 * 60 * 1000, AlarmManager.INTERVAL_HALF_HOUR, pe5);

    }

}