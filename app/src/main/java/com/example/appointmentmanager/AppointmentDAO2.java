package com.example.appointmentmanager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppointmentDAO2 {

    @Insert
    long insertAppointment(Appointment appointment);

    @Query("SELECT date_time FROM appointments WHERE date_time like :date ")
    List<String> GetToDayAppointment(String date);
}
