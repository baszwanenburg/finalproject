package com.example.bas.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Main screen, allows the user to go to the Login screen (if not logged in already)
 * or play the game (either logged in or as a guest).
 */
public class ActivityHome extends AppCompatActivity {

    private FirebaseUser user;
    private String userid;
    TextView currentUserText;
    Button login, logout, database;

    /**
     * Initializes buttons and sets listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = FirebaseAuth.getInstance().getCurrentUser();

        currentUserText = findViewById(R.id.currentUserText);
        login = findViewById(R.id.loginButton);
        logout = findViewById(R.id.logoutButton);
        database = findViewById(R.id.playButton);

        // Set all button listeners
        login.setOnClickListener(new Click());
        logout.setOnClickListener(new Click());
        database.setOnClickListener(new Click());

        updateUI();
    }

    /**
     * Changes the UI based on whether someone is currently logged in or not.
     */
    public void updateUI() {
        // If no user is logged in, show the login button
        if (user == null) {
            logout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);

            currentUserText.setText("You are not logged in.");
        // If a user is detected, hide the login button and display the logout button. Show username
        } else {
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);

            userid = user.getUid();
        }
    }

    /**
     * Adds listeners and functions to the buttons.
     */
    private class Click implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                // Go to the fragment activity, regardless of whether or not someone is logged in
                case R.id.playButton:
                    goToMyGame();
                    break;

                // Log out user and navigate to the login screen
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    goToLoginScreen();

                    Toast.makeText(getApplicationContext(), "Successfully logged out",
                            Toast.LENGTH_SHORT).show();
                    break;

                // Go to the login screen activity
                case R.id.loginButton:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        goToLoginScreen();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are currently " +
                                "already logged in.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * Starts the game.
     */
    public void goToMyGame() {
        Intent intent = new Intent(this, ActivityType.class);
        startActivity(intent);
    }

    /**
     * Navigates user to the login screen.
     */
    public void goToLoginScreen() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
}