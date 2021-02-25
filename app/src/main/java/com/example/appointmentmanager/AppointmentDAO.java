package com.example.appointmentmanager;

import android.icu.util.LocaleData;

import androidx.room.Dao;
import androidx.room.Query;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Dao
public interface AppointmentDAO {
    @Query("SELECT * FROM appointments")
    List<Appointment> allAppointments();

    @Query("SELECT * FROM appointments WHERE date_time > :day  and (SELECT  date_time < date(:day,'+1 day')) ORDER BY  date_time")
    List<Appointment> selectedDayAppointments(String day);


    @Query("SELECT DISTINCT date(date_time) FROM appointments WHERE date_time > :day  and (SELECT  date_time < date(:day,'+1 month'))")
    List<String> daysHasAppointments(String day);

}
