package com.example.appointmentmanager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Appointment.class},version = 1)
public abstract class AppointmentManagerDatabase extends RoomDatabase {
    // entities DAO
    public abstract AppointmentDAO appointmentDAO();
    public abstract AppointmentDAO2 appointmentDAO2();


    private  static AppointmentManagerDatabase ourInterface;
    public static AppointmentManagerDatabase getInstance(Context context){

        if(ourInterface == null)
        {
            ourInterface = Room.databaseBuilder(context,
                    AppointmentManagerDatabase.class,"appointments_database.db").
                    createFromAsset("databases/appointments_database.db").
                    allowMainThreadQueries().
                    build();
        }
        return  ourInterface;

    }
}
