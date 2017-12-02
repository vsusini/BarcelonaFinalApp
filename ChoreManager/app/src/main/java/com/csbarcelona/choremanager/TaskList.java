package com.csbarcelona.choremanager;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
        Button btnMenu = (Button) findViewById(R.id.btnMenu);
        Button btnAdd = (Button) findViewById(R.id.btnAddTask);


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
                Log.d("ERROR", task.get_name());
                showUpdateDeleteScreen(task);
                return true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent taskIntent = new Intent(getApplicationContext(), NewTaskActivity.class);
                startActivity(taskIntent);
            }
        });


        btnMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Toast.makeText(TaskList.this, "Moving to user Page", Toast.LENGTH_LONG).show();
                // Start new Activity


                    Intent myIntent = new Intent(TaskList.this, MenuActivity.class);
                    startActivity(myIntent);

            }
        });


    }




    private void showUpdateDeleteScreen(final Task currentTask){
        //Create Dialog with options
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_edit_task,null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(currentTask.get_name());
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        EditText txtName = (EditText) editDialog.findViewById(R.id.editName);
        txtName.setText(currentTask.get_name());
        final String name = txtName.getText().toString();

        EditText txtDescription = (EditText) editDialog.findViewById(R.id.editDescription);
        txtDescription.setText(currentTask.get_description());
        final String description = txtDescription.getText().toString();

        EditText txtDuration = (EditText) editDialog.findViewById(R.id.editDuration);
        String sDuration = String.valueOf(currentTask.get_duration());
        txtDuration.setText(sDuration);
        int duration = currentTask.get_duration();

        Spinner spinUnits = (Spinner) editDialog.findViewById(R.id.editUnits);
        for(int i = 0; i < spinUnits.getAdapter().getCount(); i++){
            String current = spinUnits.getItemAtPosition(i).toString();
            if(current.equals(currentTask.get_durationUnits())){
                spinUnits.setSelection(i);
            }
        }
        final String units = spinUnits.getSelectedItem().toString();

        Spinner spinRepeat = (Spinner) editDialog.findViewById(R.id.editRepeat);
        for(int i = 0; i < spinRepeat.getAdapter().getCount(); i++){
            String current = spinRepeat.getItemAtPosition(i).toString();
            if(current.equals(currentTask.get_repeat())){
                spinRepeat.setSelection(i);
            }
        }
        final String repeat = spinRepeat.getSelectedItem().toString();

        EditText txtPoints = (EditText) editDialog.findViewById(R.id.editPoints);
        String sPoints = String.valueOf(currentTask.get_points());
        txtPoints.setText(sPoints);
        int points = currentTask.get_points();

        Button btnDueDate = (Button) editDialog.findViewById(R.id.editDueDate);
        btnDueDate.setText(currentTask.get_dueDate());
        final String dueDate = btnDueDate.getText().toString();

        Switch switchStatus = (Switch) editDialog.findViewById(R.id.editStatus);
        String status = "";

        if(switchStatus.isActivated()){
            status = "C";
        }else{
            status = "I";
        }
        final String status2 = status;
        try{

            points = Integer.parseInt(txtPoints.getText().toString());
            duration = Integer.parseInt(txtDuration.getText().toString());

        }catch(Exception e){

        }
        final int points2 = points;
        final int duration2 = duration;

        Button btnUpdate = (Button) editDialog.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task updatedTask = new Task(currentTask.get_id(), currentTask.get_assignee(), currentTask.get_resources(), description,duration2,name,points2,currentTask.get_group(),dueDate, units,status2,repeat);
            }
        });

    }


}






