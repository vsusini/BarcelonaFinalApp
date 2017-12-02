package com.csbarcelona.choremanager;

import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ResourceCatalogActivity extends AppCompatActivity {

    EditText editTextResource;

    Button buttonAddResource;
    Button buttonDeleteResource;
    ListView listViewResources;

    List<Resource> resources;

    DatabaseReference databaseResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseResources = FirebaseDatabase.getInstance().getReference("resources");

        setContentView(R.layout.activity_resource_catalog);
        editTextResource = (EditText) findViewById(R.id.editTextResource);
        listViewResources = (ListView) findViewById(R.id.listViewResources);
        buttonAddResource = (Button) findViewById(R.id.addResourceBtn);
        buttonDeleteResource = (Button) findViewById(R.id.deleteResourceBtn);

        resources = new ArrayList<>();

        // adding an onclicklistener to the add Resources button
        buttonAddResource.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addResource();
            }
        });

        buttonDeleteResource.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteResource();
            }
        });

        listViewResources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Resource resource = resources.get(i);
                //showUpdateDeleteDialog(resource.getResourceName());
                return true;
            }
        });
    }

   /* private void showUpdateDeleteDialog(String resourceName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editResourceName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(resourceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();
    }*/

    private void addResource() {
        // getting the values to save
        String name = editTextResource.getText().toString().trim();

        //checking if the value is provided
        if(!TextUtils.isEmpty(name)){

            // creating a Resource Object
            Resource resource = new Resource(name);

            // Saving the Resource
            databaseResources.child("name").setValue(resource);

            // setting edittext to blank again
            editTextResource.setText("");

            // displaying a success toast
            Toast.makeText(this, "Resource added", Toast.LENGTH_LONG).show();
        }else{
            // if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteResource(){

    }

}
