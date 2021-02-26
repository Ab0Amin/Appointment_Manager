package com.example.appointmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, View.OnTouchListener,GestureDetector.OnGestureListener {
    ConstraintLayout parentLayout;
    MaterialCalendarView calendar;
    ListView appointments_lv;
    AppointmentsAdapter adapter;
    List<Appointment> appointments;
    ArrayList<CalendarDay> HasAppointments = new ArrayList<>();
    AppointmentDAO appointmentDAO;
    DayHasAppointmentsDecorator DHAdecorator;
    TextView selectedDayNumTextView,selectedDayInWeekTextView,selectedMonthTextView,selectedCountTextView;
    private GestureDetector mGestureDetector;

    @SuppressLint("SetTextI18n")
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
        selectedDayNumTextView = findViewById(R.id.selectedDayNumTextView);
        selectedDayInWeekTextView = findViewById(R.id.selectedDayInWeekTextView);
        selectedMonthTextView = findViewById(R.id.selectedMonthTextView);
        selectedCountTextView = findViewById(R.id.selectedCountTextView);


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
        calendar.setTopbarVisible(false);

        //new string here
        selectedDayNumTextView.setText(""+today.getDayOfMonth());
        selectedDayInWeekTextView.setText(calendar.getSelectedDate().getDate().getDayOfWeek().toString());
        selectedMonthTextView.setText(calendar.getSelectedDate().getDate().getMonth().toString());


        //setting ArrayAdapter list view
        appointments = appointmentDAO.selectedDayAppointments(today.toString());
        selectedCountTextView.setText(appointments.size() + " Appointments due");
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


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDayNumTextView.setText(""+date.getDate().getDayOfMonth());

        selectedDayInWeekTextView.setText(calendar.getSelectedDate().getDate().getDayOfWeek().toString());
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
        selectedCountTextView.setText(appointments.size() + " Appointments due");
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

        selectedMonthTextView.setText(calendar.getSelectedDate().getDate().getMonth().toString());
        getDaysHasAppointments(date);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        Toast.makeText(this, ""+view.getId() , Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

        return false;
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
            holder.beltColorImageView.setColorFilter(getColor(getItem(position).color));


            return  convertView;
        }


        int getColor(String color)
        {
            /*
            getting color id from drawable
             */
            if(color.equals("black"))
                return Color.BLACK;

            if(color.equals("blue"))
                return Color.BLUE;

            if(color.equals("red"))
                return Color.RED;

            if(color.equals("yellow"))
                return Color.YELLOW;


        return Color.WHITE;

        }

        @Override
        public void onClick(View view) {

        }
    }
    static class viewHolder {
        TextView timeTextView,titleTextView;
        ImageView beltColorImageView;
        ConstraintLayout constraintLayout;
        //TimelineView timelineView;
        public  viewHolder (View convertView)
        {
            //to stop sound and effect
            convertView.setOnClickListener(null);
            convertView.setSoundEffectsEnabled(false);

            //set views id
            timeTextView = convertView.findViewById(R.id.timeTextView);
            titleTextView = convertView.findViewById(R.id.titleTextView);
            beltColorImageView = convertView.findViewById(R.id.beltColorImageView);
            constraintLayout = convertView.findViewById(R.id.constraintLayout); //TODO onclick






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