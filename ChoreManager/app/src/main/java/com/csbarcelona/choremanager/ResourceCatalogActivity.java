package com.csbarcelona.choremanager;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceCatalogActivity extends AppCompatActivity {

    EditText editTextResource;

    Button buttonAddResource;

    ListView listViewResources;

    DataSnapshot resourceData;
    DataSnapshot taskData;

    List<Resource> resources;

    DatabaseReference databaseResources;
    DatabaseReference databaseTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseResources = FirebaseDatabase.getInstance().getReference("resources");
        databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");

        setContentView(R.layout.activity_resource_catalog);
        editTextResource = (EditText) findViewById(R.id.editTextResource);
        listViewResources = (ListView) findViewById(R.id.listViewResources);
        buttonAddResource = (Button) findViewById(R.id.addResourceBtn);

        resources = new ArrayList<>();

        // adding an onclicklistener to the add Resources button
        buttonAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResource();
            }
        });


        listViewResources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Resource resource = resources.get(i);
                //shows Delete diaglog
                showDeleteDialog(resource.getResourceName());
                return true;
            }
        });

        //Creating on change listener for filter spinner
        Spinner spinner = (Spinner) findViewById(R.id.group_spinner);
        final ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this,R.array.array_for_resource_list,android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filterAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    // clearing the previous resource List
                    resources.clear();
                    // iterating through all the nodes

                    if(i == 0) {
                        for (DataSnapshot postSnapshot : resourceData.getChildren()) {
                            // getting resource
                            Resource resource = postSnapshot.getValue(Resource.class);
                            // adding resource to the list
                            resources.add(resource);
                        }
                    }
                    else{
                        String filter = "";
                        if(i == 1) {
                            filter = "Parent";
                        }

                        if(i == 2){
                            filter = "Child";
                        }

                        List filteredResources = filterResoureByGroup(filter);
                        for (DataSnapshot postSnapshot : resourceData.getChildren()) {
                            // getting resource
                            Resource resource = postSnapshot.getValue(Resource.class);
                            // adding resource to the list
                            if(filteredResources.contains(resource.getResourceName())) {
                                resources.add(resource);
                            }
                        }

                    }


                    // creating adapter
                    ResourceList resourcesAdapter = new ResourceList(ResourceCatalogActivity.this, resources);
                    // attaching adapter to the listview
                    listViewResources.setAdapter(resourcesAdapter);

                }catch (Exception e){

                }
            }

            private List filterResoureByGroup(String group){
                List<String> filteredResources = new ArrayList<String>(0);
                for (DataSnapshot postSnapshot : taskData.getChildren()) {
                    // getting task
                    Task task = postSnapshot.getValue(Task.class);
                    if(task._group.equals(group)) {
                        List<String> resources = Arrays.asList(task._resources.split("\\s*,\\s*"));
                        //compiling all resources into one list
                        for (Object x : resources) {
                            if (!filteredResources.contains(x))
                                filteredResources.add(x.toString());
                        }
                    }
                }
                return filteredResources;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();
        // attaching value event Listener
        databaseResources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // clearing the previous resource List
                resources.clear();
                resourceData = dataSnapshot;
                // iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // getting resource
                    Resource resource = postSnapshot.getValue(Resource.class);
                    // adding resource to the list
                    resources.add(resource);
                }
                // creating adapter
                ResourceList resourcesAdapter = new ResourceList(ResourceCatalogActivity.this, resources);
                // attaching adapter to the listview
                listViewResources.setAdapter(resourcesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //retrieving data snapshot for tasks
        databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskData = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showDeleteDialog(final String resourceName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_resouce_dialog, null);
        dialogBuilder.setView(dialogView);

        // final EditText editTextName = (EditText) dialogView.findViewById(R.id.resourceName);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDeleteResource);

        dialogBuilder.setTitle(resourceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResource(resourceName);
                b.dismiss();
            }
        });


    }

    private void addResource() {
        // getting the values to save
        String name = editTextResource.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            String id = databaseResources.push().getKey();

            // creating a Resource Object
            Resource resource = new Resource(name);


            // Saving the Resource
            databaseResources.child(id).setValue(resource);

            // setting edittext to blank again
            editTextResource.setText("");

            // displaying a success toast
            Toast.makeText(this, "Resource added", Toast.LENGTH_LONG).show();
        } else {
            // if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a resouce name", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteResource(String name){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference();
        Query resourceQuery = dR.child("resources").orderByChild("resourceName").equalTo(name);

        resourceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot delSnapshot : dataSnapshot.getChildren()) {
                    delSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
