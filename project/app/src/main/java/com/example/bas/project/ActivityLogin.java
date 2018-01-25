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

/**
 * This activity allows the user to log in.
 * Logged in users will be able to save data.
 */
public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Creates the view, initializes buttons and makes sure no user is signed in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up home button (back to Main Activity)
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up button listeners for logging in and the register screen
        Button login = findViewById(R.id.loginButton);
        Button register = findViewById(R.id.goToRegister);
        login.setOnClickListener(new Click());
        register.setOnClickListener(new Click());

        // No user should be signed in on the login screen
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
    private class Click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // Get email and password and try to log in with it
                case R.id.loginButton:
                    try {
                        EditText getEmail = findViewById(R.id.GetUserEmail);
                        EditText getPassword = findViewById(R.id.GetUserPassword);

                        String email = getEmail.getText().toString();
                        String password = getPassword.getText().toString();

                        loginUser(email, password);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Incorrect input", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                // Go to the register screen activity
                case R.id.goToRegister:
                    goToRegister();
                    break;
            }
        }
    }

    /**
     * Handles user login.
     */
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(ActivityLogin.this,
                                    "Succesfully logged in", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(ActivityLogin.this,
                                    "The email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Send the user to the fragment activity if already logged in
        if (currentUser != null) {
            Toast.makeText(ActivityLogin.this, "Already logged in", Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }
    }

    /**
     * Navigates the user to the register screen.
     */
    public void goToRegister() {
        Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
        startActivity(intent);
    }

    /**
     * Updates the view based on whether or not the user is currently logged in.
     */
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Inloggen mislukt", Toast.LENGTH_SHORT).show();
        }
    }
}