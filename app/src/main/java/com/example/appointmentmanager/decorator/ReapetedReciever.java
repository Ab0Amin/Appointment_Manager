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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.appointmentmanager.AppointmentManagerDatabase;
import com.example.appointmentmanager.MainActivity;
import com.example.appointmentmanager.MyPrefernce;
import com.example.appointmentmanager.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ReapetedReciever extends BroadcastReceiver {
    Set<String>dates ;
    MyPrefernce Prefrence;
    private Context context;
    String time;
    Calendar calendar;
    long diffrence;
    @Override
    public void onReceive(Context context, Intent intent) {
         Prefrence = MyPrefernce.getInstance(context);
        this.context = context;

        dates =  Prefrence.getSetStringData("todayDates");

         time = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);


        LocalTime ConvertedTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
         calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, ConvertedTime.getHour());
        calendar.set(Calendar.MINUTE, ConvertedTime.getMinute());
         diffrence= System.currentTimeMillis()-calendar.getTimeInMillis();
        if (diffrence<30*60*1000||(diffrence>30*60*1000&&diffrence<45*60*1000) ) {


            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pe = PendingIntent.getActivity(context, 0, intent1, 0);
            createNotificationChannel();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "AppointmentApplication");
            builder.setTicker("Up Coming Appointment")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Appointment")
                    .setContentText("Click to check your up coming Appointment")
                    .setContentIntent(pe)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            Random random=new Random();
            notificationManager.notify(1,notification);
        }


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AppointmentApplication";
            String description = "AppointmentApplication";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("AppointmentApplication", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}