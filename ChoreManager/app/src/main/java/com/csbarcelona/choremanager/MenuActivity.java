package com.csbarcelona.choremanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    Button taskBtn;
    Button resourceBtn;
    Button usersBtn;

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

                //Start TaskListActivity.class
                Intent myIntent = new Intent(MenuActivity.this, ResourceCatalogActivity.class);
                startActivity(myIntent);
            }
        });
    }




}
