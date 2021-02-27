package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmanager.decorator.MyReceiver;
import com.example.appointmentmanager.decorator.ReapetedReciever;
import com.example.appointmentmanager.decorator.getDataReciever;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.Format;
import java.util.Calendar;
import java.util.List;

public class login extends AppCompatActivity {
    TextView te;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        te = findViewById(R.id.txt_test);
    }

    public void Login(View view) {

// run everydays boardcast that get dates of the day
        Intent in = new Intent(this, getDataReciever.class);
        PendingIntent pe = PendingIntent.getBroadcast(this, 0, in, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 3693725, AlarmManager.INTERVAL_DAY, pe);

        //run every half hour alarm
        Intent in2 = new Intent(this, ReapetedReciever.class);
        PendingIntent pe2 = PendingIntent.getBroadcast(this, 0, in2, 0);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 60 * 5, AlarmManager.INTERVAL_HALF_HOUR, pe2);



        // run every half hoour that get
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(login.this, dataCashing.class);
                startActivity(in);

            }
        });
        thread.start();


    }
}