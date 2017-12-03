package com.csbarcelona.choremanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

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


}
