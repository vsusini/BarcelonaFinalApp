package com.csbarcelona.choremanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
        Button btnUser = (Button) findViewById(R.id.btnUser);
        Button btnAdd = (Button) findViewById(R.id.btnAddTask);
        final ImageView imgEdit = (ImageView) findViewById(R.id.imgEdit);

        final DatabaseReference databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");
        //Load tasks from database
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
                //Display tasks
                TaskCustomAdapter customAdapter = new TaskCustomAdapter(com.csbarcelona.choremanager.TaskList.this, taskList);
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Display Tasks
        ListView listView = (ListView) findViewById(R.id.list);

        TaskCustomAdapter adapter = new TaskCustomAdapter(this,taskList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = taskList.get(i);
                showUpdateDeleteScreen(task);
                return true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

<<<<<<< HEAD
    }
=======
        btnUser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(TaskList.this, "Moving to user Page", Toast.LENGTH_LONG).show();
            }
        });

>>>>>>> 0f11fae306490d6e8ab86316448e5c425213dac4

    private void showUpdateDeleteScreen(Task currentTask){
//        //Create Dialog with options
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.activity_edit_task,null);
//        dialogBuilder.setView(dialogView);
//
//        dialogBuilder.setTitle(taskName);
//        AlertDialog editDialog = dialogBuilder.create();
//        editDialog.show();

        EditText txtName = (EditText) findViewById();
    }

    }






