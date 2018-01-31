package com.example.bas.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.round;

/**
 * This activity handles the entire game. It uses TheMovieDB API to get one of the select view
 * movie descriptions at random, and uses a TextWatcher to compare it to the user's input. Also
 * implements a timer, keeps track of the user's progress and updates Firebase accordingly.
 */
public class ActivityType extends AppCompatActivity {

    private FirebaseUser user;
    private String userid;
    private ClassMovie movieData = new ClassMovie();
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds, seconds_total, score, highScore;
    int currentMistakeCount = 0;
    int mistakeCount = 0;
    int wordCount = 0;
    int typeProgress = 0;
    int delay = 100;
    int counter = 0;
    float progress;
    float wpm = 0;
    TextView textView, timerView, speedView;
    EditText editText;
    String moviePlot, movieOverview, movieTitle, movieYear, movieReview, movieImage, movieView,
            userInput, yourTime, yourSpeed, scoreString, highScoreString, username;
    String[] movieWords;
    Handler handler;
    Handler handlerPB = new Handler();
    RoundCornerProgressBar progress1, progress2, progress3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userid = user.getUid();
        }

        textView = findViewById(R.id.movieDescription);
        editText = findViewById(R.id.userInput);
        userInput = editText.getText().toString();

        Button resultsButton = findViewById(R.id.resultsButton);

        // Use TMdb API to get a movie description at random and set it to the textview
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=f69b981254eabef0d0d10dde159b9981";
        getJSON(url);

        // Implement timer and add a TextWatcher to compare strings
        implementTimer();
        addTextWatcher();
    }

    /**
     * This TextWatcher will keep track of the user input.
     */
    public void addTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                /*
                The TextView needs to be split into seperate words in order
                to keep track of the correctly typed words per minute.
                 */
                movieView = textView.getText().toString();
                movieWords = movieView.split(" ");

                // Update the view when all typed text is fully correct
                if (mEdit.toString().equals(movieView)) {
                    Button resultsButton = findViewById(R.id.resultsButton);
                    resultsButton.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);

                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                }

                // Counts the correct words within the user's input
                String[] typedWords = mEdit.toString().split(" ");
                wordCount = 0;
                for (int i = 0; i < typedWords.length; i++) {
                    if (typedWords[i].equals(movieWords[i])) {
                        wordCount += 1;
                    }
                }

                // Compare every character of the TextView and EditText
                if (0 < mEdit.length() && mEdit.length() <= textView.length()) {
                    textView.setText(movieView, TextView.BufferType.SPANNABLE);
                    Spannable s = (Spannable) textView.getText();

                    currentMistakeCount = 0;
                    typeProgress = 0;

                    // Adds visualization to the TextView and keeps track of the user's progress
                    for (int i = 0; i < mEdit.length(); i++) {
                        int start = i;
                        int end = i + 1;

                        // Characters that are typed correctly will be colored green, otherwise red
                        if (mEdit.charAt(i) == movieView.charAt(i)) {
                            typeProgress += 1;

                            s.setSpan(new ForegroundColorSpan(0xFF66FF66),
                                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            s.setSpan(new ForegroundColorSpan(0xFFFF6666),
                                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            s.setSpan(new BackgroundColorSpan(0xFF660000),
                                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            currentMistakeCount += 1;
                        }
                    }
                    setProgressValues();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * This will handle the second progress bar, that
     * visualizes the performance of the user's best game.
     */
    public void setPersonalBestBar(final String movieTitle) {
        progress2 = findViewById(R.id.progress_2);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Scores");
        DatabaseReference mref  = dbref.child(movieTitle);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPbStats(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }

    public void getPbStats(DataSnapshot dataSnapshot) {
        if (dataSnapshot.child(userid).exists()) {
            String timerPB = dataSnapshot.child(userid).child("time").getValue().toString();
            String[] minuteTime = timerPB.split(":");
            String[] secondsMillis = minuteTime[1].split("\\.");

            int minutes = Integer.parseInt(minuteTime[0]);
            int seconds = Integer.parseInt(secondsMillis[0]);
            int millis  = Integer.parseInt(secondsMillis[1]);
            final int duration2 = 6000 * minutes + 100 * seconds + millis;

            handlerPB.postDelayed(new Runnable(){
                public void run(){
                    counter += 10;
                    progress2.setProgress((float)counter / (float)duration2);
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }
    }

    /**
     * This will handle the thirs progress bar, that visualizes the
     * performance of the registered player with the highest score.
     */
    public void setHighScoreBar(final String movieTitle) {
        progress3 = findViewById(R.id.progress_3);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Query qref = dbref.child("Scores").child(movieTitle).orderByChild("score").limitToFirst(1);

        qref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getHighScoreStats(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }

    public void getHighScoreStats(DataSnapshot dataSnapshot) {
        // Look for the data with the highest score
        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
            String Key = childSnapshot.getKey();

            TextView highScoreView = findViewById(R.id.player3);
            String bestName = dataSnapshot.child(Key).child("username").getValue().toString();
            highScoreView.setText("High score (" + bestName + ")");

            // Fetch the time and turn it into seconds (for progress bar purposes)
            String timerPB = dataSnapshot.child(Key).child("time").getValue().toString();
            String[] minuteTime = timerPB.split(":");
            String[] secondsMillis = minuteTime[1].split("\\.");

            int minutes = Integer.parseInt(minuteTime[0]);
            int seconds = Integer.parseInt(secondsMillis[0]);
            int millis  = Integer.parseInt(secondsMillis[1]);
            final int duration3 = 6000 * minutes + 100 * seconds + millis;

            // Set the high score progress bar
            handlerPB.postDelayed(new Runnable(){
                public void run(){
                    counter += 1;
                    progress3.setProgress((float)counter / (float)duration3;);
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }
    }

    /**
     * Implements and sets a timer and uses it to calculate the current number of words per minute.
     */
    public void implementTimer() {
        handler = new Handler();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    public Runnable runnable = new Runnable() {
        public void run() {
        MillisecondTime = SystemClock.uptimeMillis() - StartTime;
        UpdateTime = TimeBuff + MillisecondTime;
        seconds_total = (int) (UpdateTime / 1000);

        setTimerView();

        handler.postDelayed(this, 0);

        // Use the timer to calculate the words per minute number
        if (seconds_total > 0) {
            wpm = ((float)wordCount / (float)seconds_total) * 60;
            speedView = findViewById(R.id.wpm_1);
            speedView.setText(round(wpm) + " WPM");
        }
        }
    };

    /**
     * This will transform the lapsed time into a MM:ss:mm format.
     */
    public void setTimerView() {
        // Parses the system clock into minutes, seconds and milliseconds
        Minutes = seconds_total / 60;
        Seconds = seconds_total % 60;
        MilliSeconds = (int) (UpdateTime % 1000) / 10;

        // Use the MM:SS.mm format
        timerView = findViewById(R.id.timer);
        timerView.setText(Minutes + ":"
                + String.format("%02d", Seconds) + "."
                + String.format("%02d", MilliSeconds));
    }

    /**
     * Visualizes user's performance by adjusting the progress and mistake counter.
     */
    public void setProgressValues() {
        progress1 = findViewById(R.id.progress_1);

        // The progress is a float and calculated with the amount of characters correctly typed
        progress = (float)typeProgress / (float)movieView.length();
        progress1.setProgress(progress);

        TextView mistakeView = findViewById(R.id.mistakes);
        if (currentMistakeCount > mistakeCount) {
            mistakeCount = currentMistakeCount;
            mistakeView.setTextColor(Color.parseColor("#FF0000"));
        }
        mistakeView.setText("Mistakes: " + mistakeCount);
    }

    /**
     * Fetches JSON from the web and parses it.
     */
    public void getJSON(String url) {
        RequestQueue RQ = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Create a new JSONObject and push the data into it
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = object.getJSONArray("results");

                            /* Read the JSON object at a random index
                            Random random = new Random();
                            int i = random.nextInt(20);*/
                            JSONObject obj = arr.getJSONObject(8);

                            // Add new instance of ClassMovie
                            String id = obj.getString("id");
                            movieImage = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/"
                                    + obj.getString("poster_path");
                            movieTitle = obj.getString("title");
                            movieYear = obj.getString("release_date").substring(0,4);
                            moviePlot = obj.getString("overview");
                            movieReview = obj.getString("vote_average");

                            // Replace all direction dependent apostrophes
                            String moviePlot1 = moviePlot.replaceAll("“", "\"");
                            String moviePlot2 = moviePlot1.replaceAll("”", "\"");
                            String moviePlot3 = moviePlot2.replaceAll("‘", "\'");
                            String movieOverview = moviePlot3.replaceAll("’", "\'");

                            movieData = new ClassMovie(id, movieImage, movieTitle, movieOverview,
                                    movieYear, movieReview);
                            textView.setText(movieOverview);

                            // Handles the other progress bars
                            setPersonalBestBar(movieTitle);
                            setHighScoreBar(movieTitle);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RQ.add(stringRequest);
    }

    /**
     * Saves the stats and sends the user to the results screen.
     */
    public void goToResults (View view) throws IOException {
        yourTime  = timerView.getText().toString();
        yourSpeed = speedView.getText().toString();

        // Only handle the scores if the user is logged in
        if (user != null) {
            updateLeaderBoard();
        } else {
            Toast.makeText(ActivityType.this,
                    "Scores of guests will not be saved", Toast.LENGTH_SHORT).show();
        }

        // Use intents to pass relevant information to the results screen
        Intent intent = new Intent(this, ActivityResults.class);
        intent.putExtra("time", yourTime);
        intent.putExtra("speed", yourSpeed);
        intent.putExtra("movieClass", movieData);
        startActivity(intent);
    }

    /**
     * In case of obtaining a high score, this will save the user's score for
     * the appropriate movie description in Firebase for leaderboard purposes.
     */
    public void updateLeaderBoard() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                processUserPerformance(dataSnapshot);
                compareStats(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }

    /**
     * Looks into the current stats and Firebase data of the user.
     */
    public void processUserPerformance(DataSnapshot dataSnapshot) {
        // Extract numbers from the timer (this will be the user's score) and get username
        String timer = timerView.getText().toString();
        scoreString = timer.replaceAll("\\D+","");
        username = dataSnapshot.child("User")
                .child(userid).child("username").getValue().toString();

        // If there's no highscore yet, set it to 1000 minutes, otherwise fetch it from FB
        highScoreString = "10000000";
        if (dataSnapshot.child("Scores").child(movieTitle).child(userid).child("score").exists()) {
            highScoreString = dataSnapshot.child("Scores")
                    .child(movieTitle).child(userid).child("score").getValue().toString();
        }
    }

    /**
     * If the user manages to perform better than the last time, this will update the leaderboard.
     */
    public void compareStats(String username) {
        // Transforms scores from string into integers
        int score = Integer.parseInt(scoreString);
        int highScore = Integer.parseInt(highScoreString);

        // Do this only when the user performed better (lower time = lower score)
        if (score < highScore) {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Scores");
            DatabaseReference mref = dbref.child(movieTitle).child(userid);

            // Get the current time
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm")
                    .format(Calendar.getInstance().getTime());

            mref.child("username").setValue(username);
            mref.child("date").setValue(timeStamp);
            mref.child("time").setValue(yourTime);
            mref.child("score").setValue(score);
            mref.child("speed").setValue(yourSpeed);
        }
    }
}