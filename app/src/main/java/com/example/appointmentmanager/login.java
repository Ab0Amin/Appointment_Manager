package com.example.appointmentmanager;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.appointmentmanager.decorator.ReapetedReciever;
import com.example.appointmentmanager.decorator.getDataReciever;

import java.util.Calendar;

public class login extends AppCompatActivity {
    EditText  Password, Email;
    Calendar calendar;
    MyPrefernce Prefrence;
    String woirking;
    Intent in24, in5;
    PendingIntent pe24, pe5;
    AlarmManager alarmManager24, alarmManager5;
    String mail, password;
   static MainActivity log ;



    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SettingsActivity.log = this;

        Password = findViewById(R.id.editTextTextPassword1);
        Email = findViewById(R.id.editTextTextPersonName1);


        Prefrence = MyPrefernce.getInstance(this);
        woirking = Prefrence.getStringData("receiver");



        if (!woirking.equals("true")) {
            //run recievers
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 1);
                    calendar.set(Calendar.MINUTE, 1);
                    everyDayReciever();
                    everyhalfHourReciever();
                    Prefrence.storeData("receiver", "true");

                }
            });
            thread.start();
        }


    }


    public void Login(View view) {
        mail = Email.getText().toString();
        password = Password.getText().toString();
        if (!mail.isEmpty()&&!password.isEmpty()) {

            Backendless.UserService.login(mail, password, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    Prefrence.storeData("nameMail",mail);
                    Prefrence.storeData("pass",password);
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(login.this, "user does't exist", Toast.LENGTH_SHORT).show();
                }
            },true);
        }
        else Toast.makeText(this, "Please inter valid data", Toast.LENGTH_SHORT).show();

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

    public void newuser(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }
}