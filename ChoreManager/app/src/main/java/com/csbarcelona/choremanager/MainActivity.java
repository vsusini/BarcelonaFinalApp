package com.csbarcelona.choremanager;
        import android.content.Intent;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailField;
    private AutoCompleteTextView mPasswordField;

    private Button mLoginBtn;

    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.app_backgroud);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
        //Setting of login information
        //Author: Vincenzo Susini 86337763
        mAuth = FirebaseAuth.getInstance();
        mEmailField = (AutoCompleteTextView) findViewById(R.id.userEmail);
        mPasswordField = (AutoCompleteTextView) findViewById(R.id.userPassword);

        mLoginBtn = (Button) findViewById(R.id.loginBTN);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startSignIn();
            }
        });
        //End of login information

    }

    //Class that is activated when the button is clicked.
    //Author: Vincenzo Susini 86337763
    private void startSignIn() {
        //Setting email and password into proper strings.
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        /*For Testing Purposes if (email.equals("parent@gmail.com") && password.equals("password")){
            Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_LONG).show();
        } */
        //Checking if the username or password is empty to avoid errors
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Password or Email are Empty", Toast.LENGTH_LONG).show();
        } else {
            //Trying to login and adding a listner to see if its successful or not.
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        //If Login is unsucessful, the following is ran.
                        //Below is for testing to understand the error.
                        //For Testing Purposes System.out.println(task.getException().getMessage());
                        Toast.makeText(MainActivity.this, "Sign In Unsucessful, Please try Again!", Toast.LENGTH_LONG).show();
                    }else{
                        //If successful login, then should change pages.
                        Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                        //Change page below.
                        Intent taskIntent = new Intent(MainActivity.this, TaskList.class);
                        startActivity(taskIntent);
                    }
                }
            });
        }
    }
}

