package com.example.bas.project;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This adapter allows for the use of a customized listview,
 * in which the information of all users will be displayed.
 */
public class MyAdapter extends ArrayAdapter<ClassLeaderboard> {

    public MyAdapter(Context context, ArrayList<ClassLeaderboard> items) {
        super(context, 0, items);
    }

    /**
     * Parses the information from the array into the custom row layout.
     */
    @Override
    public View getView(int position, View view, ViewGroup group) {
        // Get the track's class at the current position
        ClassLeaderboard item = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.score_layout, group, false);

        TextView rankView  = view.findViewById(R.id.rankView);
        TextView nameView  = view.findViewById(R.id.nameView);
        TextView dateView  = view.findViewById(R.id.dateView);
        TextView timeView  = view.findViewById(R.id.timeView);
        TextView speedView = view.findViewById(R.id.speedView);

        int currentPosition = position + 1;
        String str = "" + currentPosition;

        rankView.setText(str);
        nameView.setText(item.getUsername());
        dateView.setText(item.getDate().toString());
        timeView.setText(item.getTime());
        speedView.setText(item.getSpeed());

        return view;
    }
}