package com.csbarcelona.choremanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class TaskList extends AppCompatActivity {
    String[] taskNames, taskDescription;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Button btnAdd = (Button) findViewById(R.id.btnAddTask);
        final ImageView imgEdit = (ImageView) findViewById(R.id.imgEdit);

        final DatabaseReference databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");

        databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear List of tasks
                taskList.clear();

                //itterate through all tasks
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //Get Task
                    Task task = postSnapshot.getValue(Task.class);
                    //add task to list
                    taskList.add(task);
                }

                TaskCustomAdapter customAdapter = new TaskCustomAdapter(com.csbarcelona.choremanager.TaskList.this, taskList);
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ListView listView = (ListView) findViewById(R.id.list);

        TaskCustomAdapter adapter = new TaskCustomAdapter(this,taskList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageButton btnEdit = (ImageButton) findViewById(R.id.imgEdit);
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editTaskIntent = new Intent(getApplicationContext(), EditTask.class);
                        startActivity(editTaskIntent);
                    }
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent taskIntent = new Intent(getApplicationContext(), NewTask.class);
                startActivity(taskIntent);
            }
        });





    }

    }






