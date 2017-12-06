package com.csbarcelona.choremanager;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainUserActivity extends AppCompatActivity {

    EditText editName;
    Spinner userGroupSpinner;
    Spinner updateUserSpinner;
    ListView listViewUsers;
    DatabaseReference databaseUsers;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        editName = (EditText) findViewById(R.id.editName);
        userGroupSpinner = (Spinner) findViewById(R.id.userGroupSpinner);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);


        users = new ArrayList<>();

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showUpdateDeleteDialog(user.get_name(), user.get_group(), user);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listnener
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                users.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting product
                    User user = postSnapshot.getValue(User.class);
                    //adding product to the list
                    users.add(user);
                }
                //creating adapter
                UserList usersAdapter = new UserList(MainUserActivity.this, users);
                //attaching adapter to the listview
                listViewUsers.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String userName, String userGroupSpinner, final User currentUser) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user, null);
        dialogBuilder.setView(dialogView);

        final EditText editName = (EditText) dialogView.findViewById(R.id.editName);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        final Spinner userUpdateSpinner = (Spinner) findViewById(R.id.updateUserSpinner);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editName.getText().toString().trim();
                String groupName = userUpdateSpinner.getSelectedItem().toString();
                updateUser(userName, groupName, currentUser);
                b.dismiss();
            }
        });
    }

    private void updateUser(String name, String group, User currentUser) {
        //getting the specified product reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(name);
        //updating product

        User user = currentUser;
        user.set_name(name);
        user.set_group(group);
        dR.setValue(user);

        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }



}