package com.csbarcelona.choremanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by bukst on 12/3/2017.
 */

public class ResourceItemActivity extends AppCompatActivity {

    ListView listViewResources;

    DatabaseReference databaseResourceItem;

    Button btnDeleteResource;
    EditText resourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseResourceItem = FirebaseDatabase.getInstance().getReference("resources");

        setContentView(R.layout.resource_item_layout);

        resourceName = (EditText) findViewById(R.id.resourceName);
        btnDeleteResource = (Button) findViewById(R.id.btnDeleteResource);

        final String name = resourceName.toString();
        ;
        btnDeleteResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResource(name);
            }
        });
    }

    private void deleteResource(String name) {
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
