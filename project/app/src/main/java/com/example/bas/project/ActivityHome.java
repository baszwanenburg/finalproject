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

    /**
     * Initializes buttons and sets listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = FirebaseAuth.getInstance().getCurrentUser();

        currentUserText = findViewById(R.id.currentUserText);
        Button login = findViewById(R.id.loginButton);
        Button logout = findViewById(R.id.logoutButton);
        Button database = findViewById(R.id.playButton);

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

            FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
            DatabaseReference dbref = fbdb.getReference("User/" + userid);

            /*
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    currentUserText.setText("Currently logged in as " + username);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
            */
        }

        // Set all button listeners
        login.setOnClickListener(new Click());
        logout.setOnClickListener(new Click());
        database.setOnClickListener(new Click());
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
                    finish();
                    goToLoginScreen();

                    Toast.makeText(getApplicationContext(), "Successfully logged out",
                            Toast.LENGTH_SHORT).show();
                    finish();
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