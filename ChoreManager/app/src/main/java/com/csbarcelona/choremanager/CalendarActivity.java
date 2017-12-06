package com.csbarcelona.choremanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MultiTapKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bukola on 12/3/2017.
 */

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                String sMonth = String.valueOf(month+1);
                String sDay = String.valueOf(day);
                String sYear = String.valueOf(year);

                if (sMonth.length() < 2) {
                    sMonth = "0" + (month+1);
                }
                if (sDay.length() < 2) {
                    sDay = "0" + day;
                }

                String date = sMonth + "/" + sDay + "/" + year;
                Intent intent = new Intent(CalendarActivity.this, ScheduleTaskList.class);
                intent.putExtra("date", date);
                intent.putExtra("month", sMonth);
                intent.putExtra("year", sYear);
                startActivity(intent);
            }
        });


    }

}


