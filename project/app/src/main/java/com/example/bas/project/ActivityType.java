package com.example.bas.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.round;

public class ActivityType extends AppCompatActivity {

    TextView textView, timerView, speedView;
    EditText editText;
    String movieView, movieOverview, userInput;
    String movieTitle, movieYear, movieReview, movieImage;
    String yourTime, yourSpeed;
    String[] movieWords;
    Handler handler, handler2;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds, seconds_total;
    int currentMistakeCount = 0;
    int mistakeCount = 0;
    int wordCount = 0;
    int typeProgress = 0;
    float progress;
    float wpm = 0;
    private FirebaseUser user;
    private String userid;
    private ClassMovie movieData = new ClassMovie();
    Handler handlerPB = new Handler();
    int delay = 100;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        textView = findViewById(R.id.movieDescription);
        editText = findViewById(R.id.userInput);
        userInput = editText.getText().toString();

        // use tmdb API to get a random movie description and set it to the textview
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=f69b981254eabef0d0d10dde159b9981";
        getJSON(url);

        handler = new Handler();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        addTextWatcher();
    }

    public void addTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                movieView = textView.getText().toString();
                movieWords = movieView.split(" ");

                // update view when all typed text is fully correct
                if (mEdit.toString().equals(movieView)) {
                    Button resultsButton = findViewById(R.id.resultsButton);
                    resultsButton.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);

                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                }


                String[] typedWords = mEdit.toString().split(" ");
                wordCount = 0;
                for (int i = 0; i < typedWords.length; i++) {
                    if (typedWords[i].equals(movieWords[i])) {
                        wordCount += 1;
                    }
                }

                // compare every character of the TextView and EditText
                if (0 < mEdit.length() && mEdit.length() <= textView.length()) {
                    textView.setText(movieView, TextView.BufferType.SPANNABLE);
                    Spannable s = (Spannable) textView.getText();

                    currentMistakeCount = 0;
                    typeProgress = 0;
                    for (int i = 0; i < mEdit.length(); i++) {
                        int start = i;
                        int end = i + 1;

                        if (mEdit.charAt(i) == movieView.charAt(i)) {
                            typeProgress += 1;

                            s.setSpan(new ForegroundColorSpan(0xFF66FF66), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            s.setSpan(new ForegroundColorSpan(0xFFFF6666), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            s.setSpan(new BackgroundColorSpan(0xFF660000), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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

    public void setProgressBar2(final String movieTitle) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Scores");
        DatabaseReference mref  = dbref.child(movieTitle);

        Log.d("test12345", "" + userid);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    final RoundCornerProgressBar progress2 = findViewById(R.id.progress_2);

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

                            progress = (float)counter / (float)duration2;
                            Log.d("test8", "" + progress);
                            progress2.setProgress(progress);
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }

    public void setProgressBar3(final String movieTitle) {
        final RoundCornerProgressBar progress3 = findViewById(R.id.progress_3);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Query qref = dbref.child("Scores").child(movieTitle).orderByChild("score").limitToFirst(1);

        qref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String Key = childSnapshot.getKey();

                    TextView highScoreView = findViewById(R.id.player3);
                    String bestName = dataSnapshot.child(Key).child("username").getValue().toString();
                    highScoreView.setText("High score (" + bestName + ")");

                    String timerPB = dataSnapshot.child(Key).child("time").getValue().toString();
                    String[] minuteTime = timerPB.split(":");
                    String[] secondsMillis = minuteTime[1].split("\\.");

                    int minutes = Integer.parseInt(minuteTime[0]);
                    int seconds = Integer.parseInt(secondsMillis[0]);
                    int millis  = Integer.parseInt(secondsMillis[1]);
                    final int duration3 = 6000 * minutes + 100 * seconds + millis;

                    handlerPB.postDelayed(new Runnable(){
                        public void run(){
                            counter += 1;

                            progress = (float)counter / (float)duration3;
                            progress3.setProgress(progress);
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }

    /**
     * Implements and sets a timer and uses it to calculate the current number of words per minute.
     */
    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            seconds_total = (int) (UpdateTime / 1000);

            Minutes = seconds_total / 60;
            Seconds = seconds_total % 60;
            MilliSeconds = (int) (UpdateTime % 1000) / 10;

            timerView = findViewById(R.id.timer);
            timerView.setText(Minutes + ":"
                    + String.format("%02d", Seconds) + "."
                    + String.format("%02d", MilliSeconds));

            handler.postDelayed(this, 0);

            if (seconds_total > 0) {
                wpm = ((float)wordCount / (float)seconds_total) * 60;
                speedView = findViewById(R.id.wpm_1);
                speedView.setText(round(wpm) + " WPM");
            }
        }
    };

    /**
     * Visualizes user's performance by adjusting the progress and mistake counter.
     */
    public void setProgressValues() {
        RoundCornerProgressBar progress1 = findViewById(R.id.progress_1);

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
        Log.d("test123: ", "test");
        RequestQueue RQ = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("test123: ", "test");
                            // create a new JSONObject and push the data into it
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = object.getJSONArray("results");

                            /*
                            // read the JSON object at a random index
                            Random random = new Random();
                            int i = random.nextInt(20);
                            JSONObject obj = arr.getJSONObject(i);*/

                            JSONObject obj = arr.getJSONObject(3);

                            // add new instance of ClassMovie
                            String id = obj.getString("id");
                            movieImage = "https://image.tmdb.org/t/p/w640" + obj.getString("poster_path");
                            movieTitle = obj.getString("title");
                            movieYear = obj.getString("release_date").substring(0,4);
                            movieOverview = obj.getString("overview");
                            movieReview = obj.getString("vote_average");

                            movieData = new ClassMovie(id, movieImage, movieTitle, movieOverview, movieYear, movieReview);
                            movieOverview = movieData.getOverview();
                            textView.setText(movieOverview);

                            setProgressBar2(movieTitle);
                            setProgressBar3(movieTitle);

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

        if (user != null) {
            userid = user.getUid();
            updateLeaderBoard();
        } else {
            Toast.makeText(ActivityType.this,
                    "Scores of guests will not be saved", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, ActivityResults.class);
        intent.putExtra("time", yourTime);
        intent.putExtra("speed", yourSpeed);
        intent.putExtra("movieClass", movieData);
        startActivity(intent);
    }

    /**
     * In case of obtaining a high score, his will save the user's score for
     * the appropriate movie description in Firebase for leaderboard purposes.
     */
    public void updateLeaderBoard() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Look through Firebase and find the requested information
                String timer = timerView.getText().toString();
                String scoreString = timer.replaceAll("\\D+","");
                String highScoreString = "10000000";

                String username = dataSnapshot.child("User").child(userid).child("username").getValue().toString();

                if (dataSnapshot.child("Scores").child(movieTitle).child(userid).child("score").exists()) {
                    highScoreString = dataSnapshot.child("Scores").child(movieTitle).child(userid).child("score").getValue().toString();
                }

                int score = Integer.parseInt(scoreString);
                int highScore = Integer.parseInt(highScoreString);

                if (score < highScore) {
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Scores");
                    DatabaseReference mref = dbref.child(movieTitle).child(userid);

                    mref.child("username").setValue(username);
                    mref.child("date").setValue(ServerValue.TIMESTAMP);
                    mref.child("time").setValue(yourTime);
                    mref.child("score").setValue(score);
                    mref.child("speed").setValue(yourSpeed);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getData() error", "Database or connectivity error");
            }
        });
    }
}