package com.example.appointmentmanager.decorator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.appointmentmanager.Appointment;
import com.example.appointmentmanager.MainActivity;
import com.example.appointmentmanager.R;

import java.util.Random;

public class MyReceiver extends BroadcastReceiver {
    int id;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
 id = intent.getIntExtra("id",1);
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
        notificationManager.notify(id,notification);
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AppointmentApplication";
            String description = "AppointmentApplication";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("AppointmentApplication"+id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}