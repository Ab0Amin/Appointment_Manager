package com.example.appointmentmanager.decorator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.appointmentmanager.AppointmentManagerDatabase;
import com.example.appointmentmanager.MainActivity;
import com.example.appointmentmanager.MyPrefernce;
import com.example.appointmentmanager.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class getDataReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Set<String> dates = new HashSet<>();
        MyPrefernce Prefrence = MyPrefernce.getInstance(context);
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<String> Tempdates = AppointmentManagerDatabase.getInstance(context).appointmentDAO2().GetToDayAppointment("%" + date + "%");
        for (int i = 0; i < Tempdates.size(); i++) {
            dates.add(Tempdates.get(i).replace(date, ""));
        }
        Prefrence.storeSetData("todayDates",dates);
    }

}