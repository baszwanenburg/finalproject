package com.example.bas.project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityResults extends AppCompatActivity {

    private FirebaseUser user;
    String yourTime, yourSpeed;
    ImageView coverView;
    TextView titleView, overView, yearView, reView, titleView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        ClassMovie movieData = (ClassMovie)getIntent().getSerializableExtra("movieClass");

        setMovieViews(movieData);
        setStatViews(intent);

        //getData(movieData.getMovieTitle());
        //loadLeaderboard(user);

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
        coverView  = findViewById(R.id.coverView);
        titleView  = findViewById(R.id.titleView);
        overView   = findViewById(R.id.overView);
        yearView   = findViewById(R.id.yearView);
        reView     = findViewById(R.id.reView);
        titleView2 = findViewById(R.id.titleView2);

        String movieImage    = movieData.getImage();
        String movieTitle    = movieData.getMovieTitle();
        String movieOverview = movieData.getOverview();
        String movieYear     = movieData.getYear();
        String movieReview   = movieData.getReview();

        titleView.setText(movieTitle);
        overView.setText(movieOverview);
        yearView.setText(movieYear);
        reView.setText(movieReview + "/10");
        Picasso.with(this).load(movieImage).into(coverView);
        titleView2.setText("(" + movieTitle + ")");
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
     * Retrieves Firebase data
     *
    public void getData(String movieTitle) {
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fbdb.getReference("User/" + movieTitle);
        Query qref = dbref.orderByChild("score").limitToFirst(10);

        qref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 // Look through Firebase and find the requested information
                 for (DataSnapshot child:dataSnapshot.getChildren()){
                     String username = child.getKey();
                     String date = dataSnapshot.child(userid).child("date").getValue().toString();
                     String time = dataSnapshot.child(userid).child("time").getValue().toString();
                     String speed = dataSnapshot.child(userid).child("speed").getValue().toString();

                     // Create new instance of the user class and and it to the database
                     ClassLeaderboard user = new ClassLeaderboard(userid, username, favorites, email);
                     userDatabase.add(user);
                     nameAndID.put(username,userid);
                 }
                 makeListView(nameAndID);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
                 Log.d("getData() error", "Database or connectivity error");
             }
    });


    /**
     * Displays the 10 highest scores from the user database into a listview.
     *
    public void loadLeaderboard(FirebaseUser user) {
        MyAdapter adapter;
        adapter = new MyAdapter(this, users);
        listView.setAdapter(adapter);
    }
    */
}