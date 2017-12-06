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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskList extends AppCompatActivity {
    private List<Task> taskList = new ArrayList<>();
    public static Button btnEditDueDate;
    private static String taskAssignee;
    private static String taskGroup;
    private static String filterSelection;
    private static int filterCount = 0;
    private static DataSnapshot data;
    private static List<String> resourceNames;
    public static List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Button btnMenu = (Button) findViewById(R.id.btnMenu);
        Button btnAdd = (Button) findViewById(R.id.btnAddTask);
        Spinner spinner = (Spinner) findViewById(R.id.taskGroupSpinner);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this, R.array.task_group_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filterAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterSelection = (String) adapterView.getItemAtPosition(i);
                if (filterCount == 0) {
                    Toast.makeText(getApplicationContext(), "Showing All", Toast.LENGTH_LONG).show();
                    filterCount++;
                } else {
                    Toast.makeText(getApplicationContext(), "Showing " + filterSelection, Toast.LENGTH_LONG).show();
                    updateList(data);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final DatabaseReference databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");
        //Load tasks from database
        databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        //Load tasks from database
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear List of tasks
                userList.clear();

                //itterate through all tasks
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Get Task
                    User user = postSnapshot.getValue(User.class);
                    //add task to list
                    userList.add(user);
                }
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
        String description = txtDescription.getText().toString();

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
                    String fname = areaSnapshot.child("_name").getValue(String.class);
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
        DatabaseReference dbResource = FirebaseDatabase.getInstance().getReference();
        MultiSpinner multiResource = (MultiSpinner) editDialog.findViewById(R.id.resource_multi_spinner_edit);
        dbResource.child("resources").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resourceNames = new ArrayList<>();
                String[] selectedArray = currentTask.get_resources().split(",");
                List<String> selectedResources = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String rname = areaSnapshot.child("resourceName").getValue(String.class);
                    resourceNames.add(rname);
                }

                // Set Spinner with resource names from database
                MultiSpinner multiSpinnerResource = (MultiSpinner) editDialog.findViewById(R.id.resource_multi_spinner_edit);
                multiSpinnerResource.setItems(resourceNames);
                for (int i = 0; i < selectedArray.length; i++) {
                    selectedResources.add(selectedArray[i].trim());
                }
                multiSpinnerResource.setSelected(selectedResources);
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
        Button btnDelete = (Button) editDialog.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(currentTask);
                editDialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String assignee = spinAssignee.getSelectedItem().toString();
                //RESOURCES
                MultiSpinner multiResource = (MultiSpinner) editDialog.findViewById(R.id.resource_multi_spinner_edit);
                List<String> selectedResouces = multiResource.getSelectedItems();
                String resources = "";
                for (int i = 0; i < selectedResouces.size(); i++) {
                    resources += selectedResouces.get(i) + ",";
                }

                String description = txtDescription.getText().toString();
                int duration = 0;
                final String name = txtName.getText().toString();
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
                String group = getGroupFromAssignee(assignee);
                Log.d("STATUS ERR: ", status);
                boolean updated = updateTask(currentTask.get_id(), assignee, resources, description,
                        duration, name, points, dueDate,
                        units, status, repeat, group);

                if(updated){
                    editDialog.dismiss();
                }

            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void deleteTask(Task task) {
        //Create DB reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Tasks").child(task.get_id());
        //Delete task
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_LONG).show();
    }

    public boolean updateTask(String id, String assignee, String ressources,
                           String description, int duration, final String name, int points, String dueDate, String units, String status, String repeat, String group) {
        //Update Task
        DatabaseReference dRT = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        DatabaseReference dRU = FirebaseDatabase.getInstance().getReference("Users");

        if (!TextUtils.isEmpty(assignee) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(name) && points > 0 && duration > 0 && !dueDate.equals("NONE")){
            Task updatedTask = new Task(id, assignee, ressources, description, duration, name, points, dueDate, units, status, repeat, group);
            dRT.setValue(updatedTask);

            if (status.equals("C")) {
                if (!repeat.equals("None")) {
                    //If task recurring, re-create task but at next interval.
                    DatabaseReference dRT2 = FirebaseDatabase.getInstance().getReference("Tasks");
                    Calendar c = Calendar.getInstance();
                    int month = 0, day = 0, year = 0;
                    //Get Dat Format, format date for display
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    Date repeatDate = new Date();
                    try {
                        repeatDate = df.parse(dueDate);
                        year = repeatDate.getYear();
                        month = repeatDate.getMonth();
                        day = repeatDate.getDay();
                    } catch (Exception e) {

                    }
                    //Create new instance of same task
                    Task repeatTask = updatedTask;

                    Calendar cRepeat = Calendar.getInstance();
                    cRepeat.setTime(repeatDate);

                    //Move due date accordingly
                    if (repeat.equals("Daily")) {
                        cRepeat.add(Calendar.DAY_OF_MONTH, 1);
                    } else if (repeat.equals("Weekly")) {
                        cRepeat.add(Calendar.WEEK_OF_YEAR, 1);
                    } else if (repeat.equals("Monthly")) {
                        cRepeat.add(Calendar.MONTH, 1);
                    } else if (repeat.equals("Yearly")) {
                        cRepeat.add(Calendar.YEAR, 1);
                    }

                    year = cRepeat.get(Calendar.YEAR);
                    day = cRepeat.get(Calendar.DAY_OF_MONTH);
                    month = cRepeat.get(Calendar.MONTH) + 1;

                    String sMonth = String.valueOf(month);

                    String sDay = String.valueOf(day);
                    //For display purposes
                    if (sMonth.length() < 2) {
                        sMonth = "0" + (month);
                    }
                    if (sDay.length() < 2) {
                        sDay = "0" + day;
                    }

                    String repeatDueDate = (sMonth + "/" + sDay + "/" + year);
                    //Set values
                    repeatTask.set_dueDate(repeatDueDate);
                    String newID = dRT.push().getKey();
                    repeatTask.set_status("I");
                    repeatTask.setId(newID);

                    dRT2.child(newID).setValue(repeatTask);
                }

                //Update Points accordingly
                User updatedUser = new User();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).get_name().equals(assignee)) {
                        updatedUser = userList.get(i);
                    }
                }

                updatedUser.set_totalPoints(updatedUser.get_totalPoints() + points);
                dRU.child(updatedUser.get_id()).setValue(updatedUser);


            }


            if (status.equals("I")) {
                Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Task Completed", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        Toast.makeText(getApplicationContext(), "Please fill all out fields properly!", Toast.LENGTH_LONG).show();
        return false;
    }


    public String getGroupFromAssignee(String assignee) {
        final DatabaseReference dRU = FirebaseDatabase.getInstance().getReference("Users");

        taskAssignee = assignee;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).get_name().equals(taskAssignee)) {
                taskGroup = userList.get(i).get_group();
            }
        }
        return taskGroup;
    }

    public void updateList(DataSnapshot dataSnapshot) {
        //Clear List of tasks
        data = dataSnapshot;
        taskList.clear();

        //itterate through all tasks
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            //Get Task
            Task task = postSnapshot.getValue(Task.class);
            //add task to list
            //Beginning of the filter process.
            if (task.get_status().equals("C")) { //Checking to see if a task is completed, if so, should not show.
                //do nothing because we no longer want to see it.
            } else if (filterSelection.equals("Parent")) {
                if (task._group.equals("Parent")) {
                    taskList.add(task);
                }
            } else if (filterSelection.equals("Child")) {
                if (task.get_group().equals("Child")) {
                    taskList.add(task);
                }
            } else if (filterSelection.equals("Ally")) {
                if (task.get_assignee().equals("Ally")) {
                    taskList.add(task);
                }
            } else if (filterSelection.equals("Bill")) {
                if (task.get_assignee().equals("Bill")) {
                    taskList.add(task);
                }
            } else if (filterSelection.equals("Ryan")) {
                if (task.get_assignee().equals("Ryan")) {
                    taskList.add(task);
                }
            } else if (filterSelection.equals("Nancy")) {
                if (task.get_assignee().equals("Nancy")) {
                    taskList.add(task);
                }
            } else {//Will happen if All or nothing is selected.
                taskList.add(task);
            }
        }
        //Display tasks
        TaskCustomAdapter customAdapter = new TaskCustomAdapter(com.csbarcelona.choremanager.TaskList.this, taskList);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(customAdapter);
    }
}







