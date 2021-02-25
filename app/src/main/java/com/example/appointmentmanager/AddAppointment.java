package com.example.appointmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import android.text.format.DateFormat;

public class AddAppointment extends AppCompatActivity {

    //refrences from view classes
    EditText title, note, color;
    TextView time, selectedDates;
    CalendarView calenderForDates;

    //refrences from classes
    Appointment newAppointment;
    Calendar calendar;
    MaterialTimePicker picker;
    FragmentManager fragmentManager;
    DateFormat dateFormat;

    //variables
    int year;
    int month;
    int day;
    long milliTime;

    String dateString;

    //datastructure
    List<String> AllSelecteDates;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        //find in activity
        title = findViewById(R.id.tx_title);
        note = findViewById(R.id.tx_notes);
        color = findViewById(R.id.tx_color);
        time = findViewById(R.id.TX_dateFromButton);
        selectedDates = findViewById(R.id.tx_time);
        calenderForDates = findViewById(R.id.Cl_calender);
        // take objects
        newAppointment = new Appointment();
        calendar = Calendar.getInstance();
        dateFormat = new DateFormat();
        AllSelecteDates = new ArrayList<>();

        //events
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);

//fragment for time picker
        fragmentManager = getSupportFragmentManager();

        //set callender
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calenderForDates.setMinDate(calendar.getTimeInMillis() - 1000 * 60 * 60 * 12);
        calenderForDates.setDate(calendar.getTimeInMillis());
        calenderForDates.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateString = dateFormat.format("yyyy-MMMM-dd", calendar.getTime()).toString();
                selectedDates.append(dateString + "\n");
                AllSelecteDates.add(dateString);
            }
        });


    }

    //methods
    public void insert(View view) {
        if (AllSelecteDates.size()>0&&color.getText().toString()!=null&&title.getText().toString()!=null) {
            for (int i = 0; i < AllSelecteDates.size(); i++) {

                newAppointment.color = color.getText().toString();
                newAppointment.note = note.getText().toString();
                newAppointment.title = title.getText().toString();
                newAppointment.date_time = AllSelecteDates.get(i) +" "+ time.getText().toString();
                AppointmentManagerDatabase.getInstance(this).appointmentDAO2().insertAppointment(newAppointment);

            }

        }
        else Toast.makeText(this, "Please enter a valid data", Toast.LENGTH_LONG).show();
       }

    public void getDate(View view) {
//        datePicker.show();
    }

    public void timrDialog(View view) {
//        timePicker.show();

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .build();
        picker.show(fragmentManager, "tag");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//               calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date selectedTime = calendar.getTime();

                dateString = dateFormat.format("hh.mm aa", selectedTime).toString();
                time.setText(dateString);

            }
        });


    }

    public void clearSelectedDates(View view) {
        selectedDates.setText(null);
    }

    public void back(View view) {
        onBackPressed();
    }
}