package com.example.appointmentmanager.decorator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.example.appointmentmanager.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayDecorator implements DayViewDecorator {

    private final CalendarDay today;
    private final Drawable backgroundDrawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    public TodayDecorator(Activity context) {
        today = CalendarDay.today();
        backgroundDrawable = context.getResources().getDrawable(R.drawable.today_circle_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return today.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(backgroundDrawable);
    }
}
