package com.example.appointmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.appointmentmanager.decorator.DayHasAppointmentsDecorator;
import com.example.appointmentmanager.decorator.TodayDecorator;
import com.github.vipulasri.timelineview.TimelineView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    ConstraintLayout parentLayout;
    MaterialCalendarView calendar;
    ListView appointments_lv;
    AppointmentsAdapter adapter;
    List<Appointment> appointments;
    ArrayList<CalendarDay> HasAppointments = new ArrayList<>();
    AppointmentDAO appointmentDAO;
    DayHasAppointmentsDecorator DHAdecorator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        appointmentDAO = AppointmentManagerDatabase.getInstance(this).appointmentDAO();
        //views
        parentLayout = findViewById(R.id.parent);
        calendar = findViewById(R.id.calendarView);
        appointments_lv = findViewById(R.id.appointments_lv);


        //set calendar view setting
        //get all day in the current month has appointments
        LocalDate monthFirstDay = LocalDate.now().withDayOfMonth(1);
        calendar.setSelectedDate(monthFirstDay);
        getDaysHasAppointments(calendar.getSelectedDate());
        monthFirstDay = null;

        //calendar setup
        final LocalDate today = LocalDate.now();
        calendar.setSelectedDate(today);//set day selector to today
        calendar.addDecorator(new TodayDecorator(this)); //Decorate current day selector
        calendar.setDynamicHeightEnabled(true); // make calender Height Dynamic depend on days number
        calendar.setOnDateChangedListener(this);
        calendar.setOnMonthChangedListener(this);


        //setting ArrayAdapter list view
        appointments = appointmentDAO.selectedDayAppointments(today.toString());

        if (appointments.size() != 0)
        {
            adapter = new AppointmentsAdapter(this,appointments);
            appointments_lv.setAdapter(adapter);

        }


    }

    public void moveToAddAppointment(View view) {

        Intent intent = new Intent(this,AddAppointment.class); // move to add page
        intent.putExtra("selectedDate",calendar.getSelectedDate()); // passing calendar selected date
        startActivity(intent); // start activity
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if(adapter == null)
        {
            appointments = null;
            appointments = appointmentDAO.selectedDayAppointments(date.getDate().toString());

            if(appointments.size() == 0)
            {
                return;
            }
            adapter = new AppointmentsAdapter(this,appointments);
            appointments_lv.setAdapter(adapter);
        }
        //get date Appointments from database
        appointments.clear(); // clear appointments array
        appointments.addAll(appointmentDAO.selectedDayAppointments(date.getDate().toString()));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {


        getDaysHasAppointments(date);

        Toast.makeText(this, ""+date.getDate(), Toast.LENGTH_SHORT).show();
    }

    private void getDaysHasAppointments(CalendarDay date) {
        if(DHAdecorator != null)
        {
            calendar.removeDecorator(DHAdecorator);
            DHAdecorator = null;
        }
        List<String> dates = appointmentDAO.daysHasAppointments(date.getDate().toString());

        for (String sDate : dates) {
            HasAppointments.add(CalendarDay.from(LocalDate.parse(sDate)));
        }

        DHAdecorator = new DayHasAppointmentsDecorator(Color.GREEN, HasAppointments);
        calendar.addDecorator(DHAdecorator);
        HasAppointments.clear();
    }

    class AppointmentsAdapter extends ArrayAdapter<Appointment> implements View.OnClickListener {
        public AppointmentsAdapter(@NonNull Context context,List<Appointment> appointment ) { super(context, 0,appointment); }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            viewHolder holder;

            // we made this if .. to reuse convertView and viewHolder
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.appointment_layout,parent,false);
                holder = new viewHolder(convertView);
                convertView.setTag(holder);
            }else
            {
                holder = (viewHolder) convertView.getTag();
            }

            holder.titleTextView.setText(getItem(position).title);
            holder.timeTextView.setText(getItem(position).date_time.substring(11));
            holder.beltColorImageView.setImageResource(getImgID(getItem(position).color));




            return  convertView;
        }


        int getImgID(String color)
        {
            /*
            getting image id from drawable
             */


            if(color.equals("black"))
                return R.drawable.ic_black;

            if(color.equals("blue"))
                return R.drawable.ic_blue;

            if(color.equals("red"))
                return R.drawable.ic_red;

            if(color.equals("yellow"))
                return R.drawable.ic_yellow;

            if(color.equals("white"))
                return R.drawable.ic_white;

            return R.drawable.ic_belt;
        }

        @Override
        public void onClick(View view) {

        }
    }
    static class viewHolder {
        TextView timeTextView,titleTextView;
        ImageView beltColorImageView;
        TimelineView timelineView;
        public  viewHolder (View convertView)
        {
            //to stop sound and effect
            convertView.setOnClickListener(null);
            convertView.setSoundEffectsEnabled(false);
            //style sibarator
            //set views id
            timeTextView = convertView.findViewById(R.id.timeTextView);
            titleTextView = convertView.findViewById(R.id.titleTextView);
            timelineView = convertView.findViewById(R.id.timelineView);
            beltColorImageView = convertView.findViewById(R.id.beltColorImageView);



            //time line setting
            timelineView.setMarkerColor(Color.GRAY);
            timelineView.setStartLineColor(Color.GRAY,0);
            timelineView.setEndLineColor(Color.GRAY,0);



        }
    }
    public void calendarMode(View view) {

        // to animate ConstraintLayout
        TransitionManager.beginDelayedTransition(parentLayout);

        // change CalendarMode depends on the current mode
        if(calendar.getCalendarMode() == CalendarMode.WEEKS)
            calendar.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
        else
            calendar.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
    }

}