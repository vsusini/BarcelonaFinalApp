package com.csbarcelona.choremanager;


import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.content.*;
import android.widget.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class NewTask extends AppCompatActivity {

    DatabaseReference dR;
    final int numberOfUsers = 4;
    final int numberOfResources = 6;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dR = FirebaseDatabase.getInstance().getReference("Tasks");
        Button btnComplete = (Button) findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Add Product

                //Register all fields
                EditText txtName = (EditText) findViewById(R.id.taskName);
                String name = txtName.getText().toString().trim();

                EditText txtDescription = (EditText) findViewById(R.id.description);
                String description = txtDescription.getText().toString().trim();

//                EditText txtGroup = (EditText) findViewById(R.id.taskGroup);
//                String group = txtGroup.getText().toString().trim();

                int duration,points;

                try {
                    EditText txtDuration = (EditText) findViewById(R.id.estimatedDuration);
                    duration = Integer.parseInt(txtDuration.getText().toString().trim());

                    EditText txtPoints = (EditText) findViewById(R.id.numChorePoints);
                    points = Integer.parseInt(txtPoints.getText().toString().trim());
                }catch (Exception e){
                    duration = 0;
                    points = 0;
                }

                Button btnDueDate = (Button) findViewById(R.id.btnDueDate);
                String dueDate = btnDueDate.getText().toString().trim();

                String assignees = "";
                CheckBox[] chkAssignee = {(CheckBox)findViewById(R.id.chkAss1),(CheckBox)findViewById(R.id.chkAss2),(CheckBox)findViewById(R.id.chkAss3),(CheckBox)findViewById(R.id.chkAss4)};

                for(int i = 0; i< numberOfUsers; i++){

                        if(chkAssignee[i].isChecked()){
                            if(i != numberOfUsers){
                                assignees += chkAssignee[i].getText().toString().trim() + ",";
                            }else{
                                assignees += chkAssignee[i].getText().toString().trim();
                            }

                        }
                }

                String resources = "";
                CheckBox[] chkRes = {(CheckBox)findViewById(R.id.chkRes1),(CheckBox)findViewById(R.id.chkRes2),(CheckBox)findViewById(R.id.chkRes3),(CheckBox)findViewById(R.id.chkRes4),(CheckBox)findViewById(R.id.chkRes5),(CheckBox)findViewById(R.id.chkRes6)};

                for(int i = 0; i < numberOfResources; i++){

                    if(chkRes[i].isChecked()){
                        if(i != numberOfResources){
                            resources += chkRes[i].getText().toString().trim() + ",";
                        }else{
                            resources += chkRes[i].getText().toString().trim();
                        }

                    }
                }

                Spinner spinUnits = (Spinner) findViewById(R.id.duration_spinner);
                String units = spinUnits.getSelectedItem().toString();

                Spinner spinRepeat = (Spinner) findViewById(R.id.recurring_spinner);
                String repeat = spinRepeat.getSelectedItem().toString();


                //Check if right fields are filled out

                if(!TextUtils.isEmpty(units) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(assignees) && !dueDate.equals("NONE") && duration > 0 && points > 0){
                    //Get unique ID using push().getKey()
                    String id = dR.push().getKey();

                    //Create Task
                    Task task = new Task(id, assignees,resources,description, duration, name, points, "Child", dueDate, units, "I", repeat);

                    dR.child(id).setValue(task);

                    Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_LONG).show();

                    Intent taskIntent = new Intent(getApplicationContext(), TaskList.class);
                    startActivity(taskIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill all out fields properly!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
