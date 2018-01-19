package com.example.bas.project;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.round;

public class ActivityType extends AppCompatActivity {

    TextView textView;
    TextView timerView;
    TextView speedView;
    EditText editText;
    String movieView = "";
    String movieOverview = "";
    String userInput = "";
    String movieTitle, movieYear, movieReview, movieImage;
    String[] movieWords;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds, seconds_total;
    int currentMistakeCount = 0;
    int mistakeCount = 0;
    int wordCount = 0;
    int typeProgress = 0;
    float progress;
    float wpm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        textView = findViewById(R.id.movieDescription);

        // use tmdb API to get a random movie description and set it to the textview
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=f69b981254eabef0d0d10dde159b9981";
        getJSON(url);
        //textView.setText(movieOverview);

        editText = findViewById(R.id.userInput);
        userInput = editText.getText().toString();

        handler = new Handler();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                movieView = textView.getText().toString();
                movieWords = movieView.split(" ");

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
        TextView mistakeView = findViewById(R.id.mistakes);

        progress = (float)typeProgress / (float)movieView.length();
        progress1.setProgress(progress);

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
                            // create a new JSONObject and push the data into it
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = object.getJSONArray("results");

                            // read the JSON object at a random index
                            Random random = new Random();
                            int i = random.nextInt(20);
                            JSONObject obj = arr.getJSONObject(i);

                            // add new instance of ClassMovie
                            String id = obj.getString("id");
                            movieImage = "https://image.tmdb.org/t/p/w640" + obj.getString("poster_path");
                            movieTitle = obj.getString("title");
                            movieYear = obj.getString("release_date").substring(0,4);
                            movieOverview = obj.getString("overview");
                            movieReview = obj.getString("vote_average");

                            ClassMovie movieData = new ClassMovie(id, movieImage, movieTitle, movieOverview, movieYear, movieReview);
                            movieOverview = movieData.getOverview();
                            textView.setText(movieOverview);
                            textView.setText("test 123");

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
        String yourTime  = timerView.getText().toString();
        String yourSpeed = speedView.getText().toString();

        Intent intent = new Intent(this, ActivityResults.class);
        intent.putExtra("time", yourTime);
        intent.putExtra("speed", yourSpeed);
        intent.putExtra("overview", movieOverview);
        intent.putExtra("title", movieTitle);
        intent.putExtra("year", movieYear);
        intent.putExtra("review", movieReview);
        intent.putExtra("image", movieImage);

        //Log.d("testtype", movieTitle);

        startActivity(intent);
    }
}
