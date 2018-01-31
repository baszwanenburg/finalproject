package com.example.bas.project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This adapter allows for the use of a customized listview,
 * in which the information of all users will be displayed.
 */
public class MyAdapter extends ArrayAdapter<ClassLeaderboard> {
    ClassLeaderboard item;
    String str;

    public MyAdapter(Context context, ArrayList<ClassLeaderboard> items) {
        super(context, 0, items);
    }

    /**
     * Parses the information from the array into the custom row layout.
     */
    @Override
    public View getView(int position, View view, ViewGroup group) {
        // Get the track's class at the current position
        item = getItem(position);

        // Initialize the leaderboard layout and the appropriate views
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.score_layout, group, false);

        // The user's rank is equivalent to the position in the leaderboard
        int currentPosition = position + 1;
        str = "" + currentPosition;

        handleAdapterViews(view);

        return view;
    }

    /**
     * Finds all appropriate TextViews and fills them in for each item in the adapter.
     */
    public void handleAdapterViews(View view) {
        TextView rankView  = view.findViewById(R.id.rankView);
        TextView nameView  = view.findViewById(R.id.nameView);
        TextView dateView  = view.findViewById(R.id.dateView);
        TextView timeView  = view.findViewById(R.id.timeView);
        TextView speedView = view.findViewById(R.id.speedView);

        // Set all TextViews within the leaderboard list
        rankView.setText(str);
        nameView.setText(item.getUsername());
        dateView.setText(item.getDate().toString());
        timeView.setText(item.getTime());
        speedView.setText(item.getSpeed());
    }
}