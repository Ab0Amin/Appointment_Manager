package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.appointmentmanager.decorator.ReapetedReciever;
import com.example.appointmentmanager.decorator.getDataReciever;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class dataCashing extends AppCompatActivity {
    SharedPreferences pre;
    String Currentdate = null;
    List<String> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_cashing);

        if (Currentdate == null || Currentdate != LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) {
            Currentdate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            pre = getPreferences(MODE_PRIVATE);
            times = getDates();
        } else {
            times = (List<String>) getSharedPreferences("todayDates", 0);
        }


//check calculations
        for (int i = 0; i < times.size(); i++) {
            long msec = SystemClock.elapsedRealtime() - LocalTime.parse(times.get(i), DateTimeFormatter.ISO_LOCAL_TIME).getHour() * 60 * 60 * 1000
                    - LocalTime.parse(times.get(i), DateTimeFormatter.ISO_LOCAL_TIME).getMinute() * 60 * 1000;
            if (msec < 30 * 60 * 1000 || (msec > 30 * 60 * 1000 && msec < 45 * 60 * 1000)) {
                Intent in2 = new Intent(this, ReapetedReciever.class);
                PendingIntent pe2 = PendingIntent.getBroadcast(this, 0, in2, 0);
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager2.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pe2);

            }
        }

    }

    public List<String> getDates() {
        List<String> dates = new ArrayList<>();
        Context context;
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<String> Tempdates = AppointmentManagerDatabase.getInstance(this).appointmentDAO2().GetToDayAppointment("%" + date + "%");
        for (int i = 0; i < Tempdates.size(); i++) {
            dates.add(Tempdates.get(i).replace(date, ""));
        }
        SharedPreferences.Editor editor = pre.edit();
        editor.putStringSet("todayDates", (Set<String>) dates);
        editor.apply();

        return dates;
    }
}