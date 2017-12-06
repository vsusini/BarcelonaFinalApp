package com.csbarcelona.choremanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleTaskList extends AppCompatActivity {

    ListView listViewSchedule;
    List<Task> taskList;
    DatabaseReference databaseTasks;
    private static List<String> resourceNames;
    public static Button btnEditDueDate;
    private static String taskAssignee;
    public static List<User> userList = new ArrayList<>();
    private static String taskGroup;
    private static DataSnapshot data;
    String calendarDate;
    String monthDate;
    String yearDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task_list);

        Intent intent = this.getIntent();

        if(intent != null){
            calendarDate = intent.getExtras().getString("date");
            monthDate = intent.getExtras().getString("month");
            yearDate = intent.getExtras().getString("year");

        }

        databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");

        listViewSchedule = (ListView) findViewById(R.id.listViewScheduledTasks);


        taskList = new ArrayList<>();

        listViewSchedule.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = taskList.get(i);
                //Show dialog
                showUpdateDeleteScreen(task);
                return true;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                taskList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Task task = postSnapshot.getValue(Task.class);
                    if(task.get_status().equals("C")){

                    }else if (task.get_dueDate().startsWith(monthDate) && (task.get_dueDate().endsWith(yearDate))){
                        taskList.add(task);
                    }

                }

                TaskCustomAdapter adapter = new TaskCustomAdapter(ScheduleTaskList.this, taskList);
                listViewSchedule.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteScreen(final Task currentTask) {
        //Create Dialog with options
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_edit_task, null);

        DatabaseReference dBR;


        dialogBuilder.setView(dialogView);


        dialogBuilder.setTitle(currentTask.get_name());
        final android.support.v7.app.AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        final EditText txtName = (EditText) editDialog.findViewById(R.id.editName);
        txtName.setText(currentTask.get_name());
//

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

                updateTask(currentTask.get_id(), assignee, resources, description,
                        duration, name, points, dueDate,
                        units, status, repeat, group);
                editDialog.dismiss();
            }
        });

    }


    public void deleteTask(Task task) {
        //Create DB reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Tasks").child(task.get_id());
        //Delete task
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_LONG).show();
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

    public void updateTask(String id, String assignee, String ressources,
                           String description, int duration, final String name, int points, String dueDate, String units, String status, String repeat, String group) {
        //Update Task
        DatabaseReference dRT = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        DatabaseReference dRU = FirebaseDatabase.getInstance().getReference("Users");


        Task updatedTask = new Task(id, assignee, ressources, description, duration, name, points, dueDate, units, status, repeat, group);
        dRT.setValue(updatedTask);

        if (status.equals("C")) {
            if (!repeat.equals("none")) {
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
    }

}
