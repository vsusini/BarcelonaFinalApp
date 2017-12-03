package com.csbarcelona.choremanager;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener{

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        long timeNum = calendarView.getDate();

        Date d = new Date();
        d.setTime(timeNum);
        int year, month,day = 0;

        year = d.getYear();
        month = d.getMonth();
        day = d.getDay();

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {

    }
}
