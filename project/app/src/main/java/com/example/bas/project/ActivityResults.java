package com.example.bas.project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringBufferInputStream;
import java.util.Random;

public class ActivityResults extends AppCompatActivity {

    String yourTime;
    String yourSpeed;
    String movieOverview;

    ImageView coverView;
    TextView titleView;
    TextView titleView2;
    TextView yearView;
    TextView reView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        yourTime = intent.getStringExtra("time");
        yourSpeed = intent.getStringExtra("speed");
        movieOverview = intent.getStringExtra("overview");

        coverView  = findViewById(R.id.coverView);
        titleView  = findViewById(R.id.titleView);
        titleView2 = findViewById(R.id.titleView2);
        yearView   = findViewById(R.id.yearView);
        reView     = findViewById(R.id.reView);

        TextView overView   = findViewById(R.id.overView);
        TextView timeView   = findViewById(R.id.timeView);
        TextView speedView  = findViewById(R.id.speedView);

        String movieImage  = intent.getStringExtra("image");
        String movieTitle  = intent.getStringExtra("title");
        String movieYear   = intent.getStringExtra("year");
        String movieReview = intent.getStringExtra("review");

        titleView.setText(movieTitle);
        titleView2.setText("(" + movieTitle + ")");
        yearView.setText(movieYear);
        reView.setText(movieReview);
        Picasso.with(this).load(movieImage).into(coverView);

        overView.setText(movieOverview);
        timeView.setText("Your time: " + yourTime);
        speedView.setText("Your speed: " + yourSpeed);

        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String url = "https://api.themoviedb.org/3/movie/popular?api_key=f69b981254eabef0d0d10dde159b9981";
        //getMovieInfo(url);
    }

    /**
     * Fetches JSON from the web and parses it.
     */
    public void getMovieInfo(String url) {
        RequestQueue RQ = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = object.getJSONArray("results");

                            for (int i = 0; i < 20; i++) {
                                if (arr.getJSONObject(i).getString("overview").equals(movieOverview)) {
                                    String movieTitle  = arr.getJSONObject(i).getString("title");
                                    String movieYear   = arr.getJSONObject(i).getString("release_date").substring(0,4);
                                    String movieReview = arr.getJSONObject(i).getString("vote_averae") + "/10";
                                    String movieImage  = "https://image.tmdb.org/t/p/w640" + arr.getJSONObject(i).getString("poster_path");

                                    Log.d("test123 title", movieTitle);

                                    titleView.setText(movieTitle);
                                    yearView.setText(movieYear);
                                    reView.setText(movieReview);
                                }
                            }

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
}