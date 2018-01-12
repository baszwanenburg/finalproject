package com.example.bas.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Main screen, allows the user to go to the
 * Login screen (if not logged in already) or
 * play the game (either logged in or as a guest).
 */
public class ActivityHome extends AppCompatActivity {

    /**
     * Initializes buttons and sets listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new Click());

        Button database = findViewById(R.id.playButton);
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
                    goToMyDatabase();
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

    public void goToMyDatabase() {
        Intent intent = new Intent(this, ActivityType.class);
        startActivity(intent);
    }

    public void goToLoginScreen() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
}