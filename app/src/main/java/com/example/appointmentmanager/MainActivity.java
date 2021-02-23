package com.example.appointmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout parentLayout;
    MaterialCalendarView calendar;
    ListView appointments_lv;
    AppointmentsAdapter adapter;
    List<Appointment> appointments;
    TimelineView timeline;
    RecyclerView appointmentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //views
        parentLayout = findViewById(R.id.parent);
        calendar = findViewById(R.id.calendarView);
        appointments_lv = findViewById(R.id.appointments_lv);
        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);


        //setting ArrayAdapter list view
        appointments =  AppointmentManagerDatabase.getInstance(this).appointmentDAO().allAppointments();
        adapter = new AppointmentsAdapter(this,appointments);
        appointments_lv.setAdapter(adapter);

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