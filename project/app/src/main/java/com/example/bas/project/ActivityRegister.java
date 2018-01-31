package com.example.bas.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This activity allows the user to register.
 * Their account will be saved in Firebase.
 */
public class ActivityRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Creates the view, initializes buttons and makes sure no user is signed in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolBar();

        mAuth = FirebaseAuth.getInstance();

        // Set up button listeners for registering and the login screen
        Button register = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.goToLogin);
        register.setOnClickListener(new Click());
        login.setOnClickListener(new Click());
    }

    /**
     * Displays the toolbar with a home button that redirects the user to the Home Activity.
     */
    public void setToolBar() {
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Handles all button clicks.
     */
    private class Click implements View.OnClickListener{
        @Override
        public void onClick(View view){
            switch (view.getId()){
                // In case of registration, checks if the user's input is correct
                case R.id.registerButton:
                    try{
                        checkUserInput();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "One or more fields are empty", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case R.id.goToLogin:
                    goToLogin();
                    break;
            }
        }
    }

    /**
     * Fetches the user's input and checks whether or not the provided password is sufficient.
     */
    public void checkUserInput() {
        EditText getName = findViewById(R.id.getUsername);
        EditText getEmail = findViewById(R.id.getUserEmail);
        EditText getPassword = findViewById(R.id.getUserPassword);

        String username = getName.getText().toString();
        String email = getEmail.getText().toString();
        String password = getPassword.getText().toString();

        // Checks for a sufficient password length
        if (password.length() > 5 && username.length() > 1) {
            registerUser(username, email, password);
        }
        else{
            Toast.makeText(getApplicationContext(), "Password requires a " +
                    "a minimum of 6 characters", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles user registration.
     */
    public void registerUser(final String username, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If signing in is a success, update UI with the user's information
                        if (task.isSuccessful()) {
                            createUser(username, email, mAuth.getCurrentUser().getUid());
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(ActivityRegister.this,
                                    "This email is already in use", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Adds a new user to Firebase.
     */
    public void createUser(String username, String email, String id){
        ClassUser currentUser = new ClassUser(id, username, email);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("User");
        dbref.child(id).setValue(currentUser);
    }

    /**
     * Navigates the user to the login screen.
     */
    public void goToLogin() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
    }

    /**
     * Updates the user interface based on whether or not a user is logged in.
     */
    public void updateUI(FirebaseUser currentUser) {

        // If a user is detected, it means the registration was succesful, otherwise it failed
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(),
                    "Successfully registered", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityRegister.this, ActivityHome.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}
