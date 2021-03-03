package com.example.appointmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.appointmentmanager.decorator.DayHasAppointmentsDecorator;
import com.example.appointmentmanager.decorator.TodayDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, CompoundButton.OnCheckedChangeListener {
    int processors = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(processors);

    ConstraintLayout parentLayout;
    MaterialCalendarView calendar;
    CalendarDay previousDaySelected;
    Switch modeSwitch;
    ListView appointments_lv;
    AppointmentsAdapter adapter;
    List<Appointment> appointments;
    ArrayList<CalendarDay> HasAppointments = new ArrayList<>();
    AppointmentDAO appointmentDAO;
    DayHasAppointmentsDecorator DHAdecorator;

    TextView selectedDayNumTextView, selectedDayInWeekTextView, selectedCountTextView;
    String mail, password;
    MyPrefernce Prefrence;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login.log = this;
        Prefrence = MyPrefernce.getInstance(this);
        password = Prefrence.getStringData("pass");
        mail = Prefrence.getStringData("nameMail");
        Backendless.initApp(this, "A45DF29A-4407-DF76-FF5C-8F3DC042CA00", "07C2241D-B448-47B2-A2E4-913F2B05FFF3");

        Backendless.UserService.login(mail, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(MainActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void handleFault(BackendlessFault fault) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);

            }
        }, true);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        appointmentDAO = AppointmentManagerDatabase.getInstance(this).appointmentDAO();
        //views
        parentLayout = findViewById(R.id.parent);
        calendar = findViewById(R.id.calendarView);
        appointments_lv = findViewById(R.id.appointments_lv);
        selectedDayNumTextView = findViewById(R.id.selectedDayNumTextView);
        selectedDayInWeekTextView = findViewById(R.id.selectedDayInWeekTextView);
        selectedCountTextView = findViewById(R.id.selectedCountTextView);
        modeSwitch = findViewById(R.id.modeSwitch);
        modeSwitch.setOnCheckedChangeListener(this);


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
        calendar.setLeftArrow(0);
        calendar.setRightArrow(0);

        //calendar.setTopbarVisible(false);

        //new string here
        selectedDayNumTextView.setText("" + today.getDayOfMonth());
        selectedDayInWeekTextView.setText(calendar.getSelectedDate().getDate().getDayOfWeek().toString());


        //setting ArrayAdapter list view
        pool.execute(() -> {
            appointments = appointmentDAO.selectedDayAppointments(today.toString()); //TODO it may take time
            selectedCountTextView.setText(appointments.size() + " Appointments due");
            if (appointments.size() != 0) {
                adapter = new AppointmentsAdapter(MainActivity.this, appointments);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appointments_lv.setAdapter(adapter);
                    }
                });


            }
        });
        previousDaySelected = calendar.getSelectedDate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAppointments(calendar.getSelectedDate()); //update the appointments
    }

    public void moveToAddAppointment(View view) {

        Intent intent = new Intent(this, AddAppointment.class); // move to add page
        intent.putExtra("selectedDate", calendar.getSelectedDate()); // passing calendar selected date
        startActivity(intent); // start activity
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        //don't do any thing if same day has selected
        if (date == previousDaySelected)
            return;

        //setting date on the top left of list view
        selectedDayNumTextView.setText("" + date.getDate().getDayOfMonth()); //month day
        selectedDayInWeekTextView.setText(calendar.getSelectedDate().getDate().getDayOfWeek().toString()); //week day


        getAppointments(date);


    }

    private void getAppointments(@NonNull CalendarDay date) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    appointments = appointmentDAO.selectedDayAppointments(date.getDate().toString()); //TODO it may take time

                    if (appointments.size() == 0) {
                        appointments = null;
                        return;
                    }
                    adapter = new AppointmentsAdapter(MainActivity.this, appointments);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appointments_lv.setAdapter(adapter);
                        }
                    });

                }

                //get date Appointments from database
                appointments.clear(); // clear appointments array
                appointments.addAll(appointmentDAO.selectedDayAppointments(date.getDate().toString())); //TODO it may take time
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        selectedCountTextView.setText(appointments.size() + " Appointments due");
                    }
                });


                previousDaySelected = calendar.getSelectedDate(); // set previousDaySelected reference
            }
        });
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        // set a green dot at the bottom of each day has an appointment
        pool.execute(new Runnable() {
            @Override
            public void run() {
                getDaysHasAppointments(date);
            }
        });

    }

    private void getDaysHasAppointments(CalendarDay date) {

        // clean  decorator object and clear screen
        if (DHAdecorator != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calendar.removeDecorator(DHAdecorator);
                }
            });

            DHAdecorator = null;
        }
        // get the selected moth days  that has Appointments -> Sting list
        List<String> dates = appointmentDAO.daysHasAppointments(date.getDate().toString());//TODO it may take time

        // convert  String dates to ArrayList of CalendarDay
        for (String sDate : dates) {
            HasAppointments.add(CalendarDay.from(LocalDate.parse(sDate)));
        }
        // decorate days has appointment in this moth
        DHAdecorator = new DayHasAppointmentsDecorator(Color.GREEN, HasAppointments);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                calendar.addDecorator(DHAdecorator);
            }
        });

        HasAppointments.clear(); // clear array to reuse it
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        // to animate ConstraintLayout
        TransitionManager.beginDelayedTransition(parentLayout);

        // change CalendarMode depends on the current mode
        if (calendar.getCalendarMode() == CalendarMode.WEEKS)
            calendar.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
        else
            calendar.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
    }

    public void moveToSettingsImageView(View view) {
        Intent intent = new Intent(this, SettingsActivity.class); // move to add page
        startActivity(intent); // start activity
    }


    class AppointmentsAdapter extends ArrayAdapter<Appointment> implements View.OnClickListener {
        public AppointmentsAdapter(@NonNull Context context, List<Appointment> appointment) {
            super(context, 0, appointment);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            viewHolder holder;

            // we made this if .. to reuse convertView and viewHolder
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.appointment_layout, parent, false);
                holder = new viewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }


            holder.timeTextView.setText(getTime(getItem(position).date_time.substring(11, 16)));
            holder.titleTextView.setText(getItem(position).title);
            holder.beltColorImageView.setColorFilter(getColor(getItem(position).color));
            holder.constraintLayout.setOnClickListener(this);
            holder.constraintLayout.setTag(getItem(position));
            return convertView;
        }

        //set time format
        String getTime(String time) {


            byte h = Byte.parseByte(time.substring(0, 2));
            if (h > 12) {
                h -= 12;

                if (h <= 9)
                    return "0" + h + time.substring(2) + " PM";
                else
                    return h + time.substring(2) + " PM";
            }
            return time + " AM";
        }

        int getColor(String color) {
            /*
            getting color id from drawable
             */
            if (color.equals("black"))
                return Color.BLACK;

            if (color.equals("blue"))
                return Color.BLUE;

            if (color.equals("red"))
                return Color.RED;

            if (color.equals("yellow"))
                return Color.YELLOW;


            return Color.WHITE;

        }

        @Override
        public void onClick(View view) {
            Appointment a = (Appointment) view.getTag();

            Intent intent = new Intent(MainActivity.this, AppointmentPage.class); // move to add page
            intent.putExtra("title", a.title);
            intent.putExtra("note", a.note);
            intent.putExtra("color", getColor(a.color));
            intent.putExtra("date", a.date_time);
            intent.putExtra("id", a.id);
            startActivity(intent); // start activity

        }
    }

    static class viewHolder {
        TextView timeTextView, titleTextView;
        ImageView beltColorImageView;
        ConstraintLayout constraintLayout;

        //TimelineView timelineView;
        public viewHolder(View convertView) {
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


}