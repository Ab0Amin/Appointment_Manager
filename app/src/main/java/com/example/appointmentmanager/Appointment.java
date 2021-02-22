package com.example.appointmentmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointments")
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title,date_time,note,color;
}
