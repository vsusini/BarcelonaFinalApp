package com.csbarcelona.choremanager;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NewTaskActivity extends AppCompatActivity {

    DatabaseReference dR;
    int numberOfResources;
    DatabaseReference dBR;

    int userSpinnerPosition;
    DatabaseReference dbResource;
    String resourceDefaultText;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);



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

                Spinner nameSpinner = (Spinner) findViewById(R.id.userGroupSpinner);
                ArrayAdapter<String> fnameAdapter = new ArrayAdapter<String>(NewTaskActivity.this, android.R.layout.simple_spinner_item, listNames);

                fnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(fnameAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbResource = FirebaseDatabase.getInstance().getReference();

       dbResource.child("resources").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> resourceNames = new ArrayList<>();

                for(DataSnapshot areaSnapshot: dataSnapshot.getChildren()){
                    String rname = areaSnapshot.child("resourceName").getValue(String.class);
                    resourceNames.add(rname);
                }

                // Set Spinner with resource names from database
                resourceDefaultText = "";
                final MultiSpinner multiSpinnerResource = (MultiSpinner) findViewById(R.id.resource_multi_spinner);
               multiSpinnerResource.setItems(resourceNames);




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        dR = FirebaseDatabase.getInstance().getReference("Tasks");
        Button btnComplete = (Button) findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add Product

                //Register all fields
                EditText txtName = (EditText) findViewById(R.id.taskName);
                String name = txtName.getText().toString().trim();

                EditText txtDescription = (EditText) findViewById(R.id.description);
                String description = txtDescription.getText().toString().trim();



                int duration, points;

                try {
                    EditText txtDuration = (EditText) findViewById(R.id.estimatedDuration);
                    duration = Integer.parseInt(txtDuration.getText().toString().trim());

                    EditText txtPoints = (EditText) findViewById(R.id.numChorePoints);
                    points = Integer.parseInt(txtPoints.getText().toString().trim());
                } catch (Exception e) {
                    duration = 0;
                    points = 0;
                }

                Button btnDueDate = (Button) findViewById(R.id.btnDueDate);
                String dueDate = btnDueDate.getText().toString().trim();

                Spinner spinAssigned = (Spinner) findViewById(R.id.userGroupSpinner);
                String assigned = spinAssigned.getSelectedItem().toString();


               MultiSpinner multiSpinnerResource = (MultiSpinner) findViewById(R.id.resource_multi_spinner);
                List <String> spinnerResources = new ArrayList<>();
                spinnerResources = multiSpinnerResource.getSelectedItems();


                String resources = "";

                int numberOfResources = spinnerResources.size();

                for(int i = 0; i < numberOfResources-1; i++)
                    resources += spinnerResources.get(i) + ", ";
                try{
                    resources = resources + spinnerResources.get(numberOfResources-1);
                }catch (Exception e){
                    resources = "";
                }

                Spinner spinUnits = (Spinner) findViewById(R.id.duration_spinner);
                String units = spinUnits.getSelectedItem().toString();

               Spinner spinRepeat = (Spinner) findViewById(R.id.recurring_spinner);
                String repeat = spinRepeat.getSelectedItem().toString();


                String group = "";

                //GROUP
                for(int i=0; i<TaskList.userList.size();i++){
                    if(TaskList.userList.get(i).get_name().equals(assigned)){
                        group = TaskList.userList.get(i).get_group();
                    }
                }

                //Check if right fields are filled out

                if (!TextUtils.isEmpty(units) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(assigned)&& !dueDate.equals("NONE") && duration > 0 && points > 0) {
                    //Get unique ID using push().getKey()
                    String id = dR.push().getKey();

                    //Create Task
                    Task task = new Task(id, assigned, resources, description, duration, name, points, dueDate, units, "I", repeat, group);

                    dR.child(id).setValue(task);

                    Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_LONG).show();

                    Intent taskIntent = new Intent(getApplicationContext(), TaskList.class);
                    startActivity(taskIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all out fields properly!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // shows the date picker dialogue
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


}
