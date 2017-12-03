package com.example.sizum.userlist2;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainUserActivity extends AppCompatActivity {

    EditText editName;
    EditText editDescription;
    Button buttonAddUser;
    ListView listViewUsers;
    DatabaseReference databaseUsers;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        editName = (EditText) findViewById(R.id.editName);
        editDescription = (EditText) findViewById(R.id.editDescription);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonAddUser = (Button) findViewById(R.id.addButton);

        users = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showUpdateDeleteDialog(user.getName(), user.getDescription());
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


    private void showUpdateDeleteDialog(final String userName, String userDescription) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user, null);
        dialogBuilder.setView(dialogView);

        final EditText editName = (EditText) dialogView.findViewById(R.id.editName);
        final EditText editDescription = (EditText) dialogView.findViewById(R.id.editDescription);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                if (!TextUtils.isEmpty(description)) {
                    updateUser(userName, description);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(userName);
                b.dismiss();
            }
        });
    }

    private void updateUser(String name, String description) {
        //getting the specified product reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(name);
        //updating product
        User user = new User(name, description);
        dR.setValue(user);

        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteUser(String name) {
        //getting the specified product reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(name);
        //removing product
        dR.removeValue();
        Toast.makeText(getApplication(), "User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addUser() {
        //getting the values to save
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our User
            String username = databaseUsers.push().getKey();

            //creating an User Object
            User user = new User(username.toString(), description);

            //Saving the User
            databaseUsers.child(username).setValue(user);

            //setting edittext to blank again
            editName.setText("");
            editDescription.setText("");

            //displaying a success toast
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

}