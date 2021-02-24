package com.example.appointmentmanager;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppointmentDAO {
    @Query("SELECT * FROM appointments")
    List<Appointment> allAppointments();

    //im here
}
