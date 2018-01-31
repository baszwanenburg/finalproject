package com.example.bas.project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Displays the user stats from the game that just finished, and shows the high score of every user
 * that typed the appropriate movie description in a leaderboard listview.
 */
public class ActivityResults extends AppCompatActivity {

    private FirebaseUser user;
    private ArrayList<ClassLeaderboard> items = new ArrayList<>();
    ClassMovie movieData;
    String movieImage, movieTitle, movieOverview, movieYear, movieReview,
            yourTime, yourSpeed, userid;
    ImageView coverView;
    TextView titleView, overView, yearView, reView, titleView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userid = user.getUid();
        }

        // Get the data from the previous activity and display it
        Intent intent = getIntent();
        movieData = (ClassMovie)getIntent().getSerializableExtra("movieClass");

        // Set all views within the activity
        setMovieViews(movieData);
        setStatViews(intent);
        getData(movieData.getMovieTitle());
        setToolBar();
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
     * Initializes the movie information views and uses
     * the movie class to set the views accordingly.
     */
    public void setMovieViews(ClassMovie movieData) {
        getMovieViewsAndContent();

        titleView.setText(movieTitle);
        overView.setText(movieOverview);
        yearView.setText(movieYear);
        reView.setText(movieReview + "/10");
        Picasso.with(this).load(movieImage).into(coverView);
        titleView2.setText("(" + movieTitle + ")");
    }

    public void getMovieViewsAndContent() {
        coverView  = findViewById(R.id.coverView);
        titleView  = findViewById(R.id.titleView);
        overView   = findViewById(R.id.overView);
        yearView   = findViewById(R.id.yearView);
        reView     = findViewById(R.id.reView);
        titleView2 = findViewById(R.id.titleView2);

        movieImage    = movieData.getImage();
        movieTitle    = movieData.getMovieTitle();
        movieOverview = movieData.getOverview();
        movieYear     = movieData.getYear();
        movieReview   = movieData.getReview();
    }

    /**
     * Fetches the user's stats from the last game played and displays them.
     */
    public void setStatViews(Intent intent) {
        yourTime = intent.getStringExtra("time");
        yourSpeed = intent.getStringExtra("speed");

        TextView timeView   = findViewById(R.id.timeView);
        TextView speedView  = findViewById(R.id.speedView);

        timeView.setText("Your time: " + yourTime);
        speedView.setText("Your speed: " + yourSpeed);
    }

    /**
     * Retrieves Firebase data needed to set up the leaderboard
     */
    public void getData(String movieTitle) {
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fbdb.getReference("Scores").child(movieTitle);

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Look through Firebase and find the scores from the appropriate movie description
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClassLeaderboard post = postSnapshot.getValue(ClassLeaderboard.class);
                    items.add(post);
                }

                loadLeaderboard(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }


    /**
     * Displays the users with their scores from the user database into a listview.
     */
    public void loadLeaderboard(ArrayList<ClassLeaderboard> items) {
        MyAdapter adapter;
        adapter = new MyAdapter(this, items);
        adapter.sort(new Comparator<ClassLeaderboard>() {
            @Override
            public int compare(ClassLeaderboard object1, ClassLeaderboard object2) {
                return ((Integer) object1.getScore()).compareTo(object2.getScore());
            }
        });
        adapter.notifyDataSetChanged();

        ListView listView = findViewById(R.id.leaderboardListView);
        listView.setAdapter(adapter);
    }
}