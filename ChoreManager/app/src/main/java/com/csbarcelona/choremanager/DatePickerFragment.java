package com.csbarcelona.choremanager;

/**
 * Created by Bukola on 11/30/2017.
 * Inspired by: https://stackoverflow.com/questions/11527051/get-date-from-datepicker-using-dialogfragment
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year, month, day = 0;

        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Once the date is set, the date will display on the button
        String activity = getActivity().toString();

        String sMonth = String.valueOf(month+1);
        String sDay = String.valueOf(day);

        if (sMonth.length() < 2) {
            sMonth = "0" + (month+1);
        }
        if (sDay.length() < 2) {
            sDay = "0" + day;
        }


        if (activity.contains("TaskList")) {
            TaskList.btnEditDueDate.setText(sMonth + "/" + sDay + "/" + year);
        } else if (activity.contains("NewTaskActivity")) {
            Button activityButton = (Button) getActivity().findViewById(R.id.btnDueDate);
            activityButton.setText(sMonth + "/" + sDay + "/" + year);
        }


    }
}

