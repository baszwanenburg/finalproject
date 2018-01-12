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

import java.util.HashMap;

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

        mAuth = FirebaseAuth.getInstance();

        // Set up home button (back to Main Activity)
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up button listeners for registering and the login screen
        Button register = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.goToLogin);
        register.setOnClickListener(new Click());
        login.setOnClickListener(new Click());

        // No user should be signed in on the register screen
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    mAuth.signOut();
                }
            }
        };
    }

    /**
     * Handles all button clicks.
     */
    private class Click implements View.OnClickListener{
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.registerButton:
                    try{
                        EditText getName = findViewById(R.id.getUsername);
                        EditText getEmail = findViewById(R.id.getUserEmail);
                        EditText getPassword = findViewById(R.id.getUserPassword);

                        String username = getName.getText().toString();
                        String email = getEmail.getText().toString();
                        String password = getPassword.getText().toString();

                        if (password.length() > 5 && username.length() > 1) {
                            registerUser(username, email, password);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Password needs to be " +
                                    "at least 6 characters long", Toast.LENGTH_SHORT).show();
                        }
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
     * Handles user registration.
     */
    public void registerUser(final String username, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If signing in is a success, update UI with the user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String id = currentUser.getUid();
                            createUser(username, email, id);
                            updateUI(currentUser);
                        } else {
                            Toast.makeText(ActivityRegister.this,
                                    "Authenticatie mislukt", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createUser(String username, String email, String id){
        HashMap favorites = new HashMap<>();
        ClassUser currentUser = new ClassUser(id, username, favorites, email);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("User");
        dbref.child("user").child(id).setValue(currentUser);
    }

    public void goToLogin() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(),
                    "Registratie geslaagd", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityRegister.this, ActivityHome.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Registratie mislukt", Toast.LENGTH_SHORT).show();
        }
    }
}
