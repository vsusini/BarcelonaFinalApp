package com.csbarcelona.choremanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Map;

public class MenuActivity extends Activity {

    Button taskBtn;
    Button resourceBtn;
    Button userBtn;
    Button calendarBtn;
    Button helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // Locate the task button in the activity menu
        taskBtn = (Button) findViewById(R.id.btn_tasks);

        // Capture button clicks
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start TaskListActivity.class
                Intent myIntent = new Intent(MenuActivity.this, TaskList.class);
                startActivity(myIntent);
            }
        });

        // Locate the task button in the activity menu
        resourceBtn = (Button) findViewById(R.id.btn_resources);

        // Capture button clicks
        resourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start ResourceCatalogActivity.class
                Intent myIntent = new Intent(MenuActivity.this, ResourceCatalogActivity.class);
                startActivity(myIntent);
            }
        });

        //Locate the Calendar button the the activity menu
        calendarBtn = (Button)findViewById(R.id.btn_calendar);

        // Capture button clicks
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start CalendarActivity.class
                Intent myIntent = new Intent(MenuActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            }
        });


        //L Locate the User button in the activity menu
        userBtn = (Button)findViewById(R.id.btn_users);

        // Capture button clicks
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Star Calendar Activty

                Intent myIntent = new Intent(MenuActivity.this, MainUserActivity.class);
                startActivity(myIntent);
            }

        });



        helpBtn = (Button)findViewById(R.id.btn_help);

        // Capture button clicks
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Star Calendar Activty

                Intent myIntent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(myIntent);
            }

        });


    }

}
