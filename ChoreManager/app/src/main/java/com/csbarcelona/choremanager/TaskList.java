package com.csbarcelona.choremanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.*;


import com.google.firebase.database.*;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskList extends AppCompatActivity {
    String[] taskNames, taskDescription;
    private List<Task> taskList = new ArrayList<>();
    public static Button btnEditDueDate;

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
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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

        TaskCustomAdapter adapter = new TaskCustomAdapter(this, taskList);
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
            public void onClick(View v) {
                Intent taskIntent = new Intent(getApplicationContext(), NewTaskActivity.class);
                startActivity(taskIntent);
            }
        });


        btnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(TaskList.this, "Moving to user Page", Toast.LENGTH_LONG).show();
                // Start new Activity


                Intent myIntent = new Intent(TaskList.this, MenuActivity.class);
                startActivity(myIntent);

            }
        });


    }


    private void showUpdateDeleteScreen(final Task currentTask) {
        //Create Dialog with options
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_edit_task, null);

        DatabaseReference dBR;


        dialogBuilder.setView(dialogView);


        dialogBuilder.setTitle(currentTask.get_name());
        final AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        final EditText txtName = (EditText) editDialog.findViewById(R.id.editName);
        txtName.setText(currentTask.get_name());
//        String name = txtName.getText().toString();

        final EditText txtDescription = (EditText) editDialog.findViewById(R.id.editDescription);
        txtDescription.setText(currentTask.get_description());
//        String description = txtDescription.getText().toString();

        final EditText txtDuration = (EditText) editDialog.findViewById(R.id.editDuration);
        String sDuration = String.valueOf(currentTask.get_duration());
        txtDuration.setText(sDuration);

        // Used to populate assigned users spinner with names in datebase.
        dBR = FirebaseDatabase.getInstance().getReference();
        dBR.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> listNames = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String fname = areaSnapshot.child("name").getValue(String.class);
                    listNames.add(fname);
                }
                listNames.add(0, "");

                Spinner nameSpinner = (Spinner) editDialog.findViewById(R.id.assignedUserSpinner2);
                ArrayAdapter<String> fnameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listNames);

                fnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(fnameAdapter);

                for (int i = 0; i < listNames.size(); i++) {
                    String comparable = nameSpinner.getItemAtPosition(i).toString();
                    if (comparable.equals(currentTask._assignee)) {
                        nameSpinner.setSelection(i);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final Spinner spinAssignee = (Spinner) editDialog.findViewById(R.id.assignedUserSpinner2);

        final Spinner spinUnits = (Spinner) editDialog.findViewById(R.id.editUnits);
        for (int i = 0; i < spinUnits.getAdapter().getCount(); i++) {
            String current = spinUnits.getItemAtPosition(i).toString();
            if (current.equals(currentTask.get_durationUnits())) {
                spinUnits.setSelection(i);
            }
        }
        String units = spinUnits.getSelectedItem().toString();

        final Spinner spinRepeat = (Spinner) editDialog.findViewById(R.id.editRepeat);
        for (int i = 0; i < spinRepeat.getAdapter().getCount(); i++) {
            String current = spinRepeat.getItemAtPosition(i).toString();
            if (current.equals(currentTask.get_repeat())) {
                spinRepeat.setSelection(i);
            }
        }

        final EditText txtPoints = (EditText) editDialog.findViewById(R.id.editPoints);
        String sPoints = String.valueOf(currentTask.get_points());
        txtPoints.setText(sPoints);

        final Button btnDueDate = (Button) editDialog.findViewById(R.id.editDueDate);
        btnEditDueDate = btnDueDate;
        btnEditDueDate.setText(currentTask.get_dueDate());


        final Switch switchStatus = (Switch) editDialog.findViewById(R.id.editStatus);

        if (currentTask.get_status().equals("C")) {
            switchStatus.setChecked(true);
        }


        Button btnUpdate = (Button) editDialog.findViewById(R.id.btnUpdate);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String assignee = spinAssignee.getSelectedItem().toString();
                //RESOURCES
                String description = txtDescription.getText().toString();
                int duration = 0;
                String name = txtName.getText().toString();
                int points = 0;
                try {
                    duration = Integer.parseInt(txtDuration.getText().toString());
                    points = Integer.parseInt(txtPoints.getText().toString());

                } catch (Exception e) {

                }
                String dueDate = btnDueDate.getText().toString();
                String units = spinUnits.getSelectedItem().toString();
                String status = "";
                if (switchStatus.isChecked()) {
                    status = "C";
                    Log.d("STATUS-TRUE: ", status);
                } else {
                    status = "I";
                    Log.d("STATUS-FALSE: ", status);
                }
                String repeat = spinRepeat.getSelectedItem().toString();

                //GROUPS
                String group = "Child";


                updateTask(currentTask.get_id(), assignee, currentTask.get_resources(), description,
                        duration, name, points, dueDate,
                        units, status, repeat, group);
                editDialog.dismiss();
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void updateTask(String id, String assignee, String ressources,
                           String description, int duration, String name, int points, String dueDate, String units, String status, String repeat, String group) {


        //Update Task
        DatabaseReference dRT = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        Task updatedTask = new Task(id, assignee, ressources, description, duration, name, points, dueDate, units, status, repeat, group);
        dRT.setValue(updatedTask);


        if (status.equals("C") && !repeat.equals("none")) {
            //If task recurring, re-create task but at next interval.
            DatabaseReference dRT2 = FirebaseDatabase.getInstance().getReference("Tasks");
            Calendar c = Calendar.getInstance();
            int month = 0, day = 0, year = 0;

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date repeatDate = new Date();
            try{
                repeatDate = df.parse(dueDate);
                year = repeatDate.getYear();
                month = repeatDate.getMonth();
                day = repeatDate.getDay();
            }catch (Exception e){

            }

            Task repeatTask = updatedTask;

            Calendar cRepeat = Calendar.getInstance();
            cRepeat.setTime(repeatDate);


            if(repeat.equals("Daily")){
                cRepeat.add(Calendar.DAY_OF_MONTH,1);
            }else if(repeat.equals("Weekly")){
                cRepeat.add(Calendar.WEEK_OF_YEAR,1);
            }else if(repeat.equals("Monthly")){
                cRepeat.add(Calendar.MONTH,1);
            }else if(repeat.equals("Yearly")){
                cRepeat.add(Calendar.YEAR,1);
            }

            year = cRepeat.get(Calendar.YEAR);
            day = cRepeat.get(Calendar.DAY_OF_MONTH);
            month = cRepeat.get(Calendar.MONTH)+1;

            String sMonth = String.valueOf(month);

            String sDay = String.valueOf(day);

            if (sMonth.length() < 2) {
                sMonth = "0" + (month);
            }
            if (sDay.length() < 2) {
                sDay = "0" + day;
            }

            String repeatDueDate = (sMonth + "/" + sDay + "/" + year);

            repeatTask.set_dueDate( repeatDueDate );
            String newID = dRT.push().getKey();
            repeatTask.set_status("I");
            repeatTask.setId(newID);

            dRT2.child(newID).setValue(repeatTask);
        }

        //If task completed add points to user
        DatabaseReference dRU = FirebaseDatabase.getInstance().getReference("Users");

        if (status.equals("I")) {
            Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Task Completed", Toast.LENGTH_LONG).show();
        }

    }


}






