package com.example.bas.project;

import android.content.Context;
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
public class MyAdapter extends ArrayAdapter<ClassUser> {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase fbdb;
    private DatabaseReference dbref;

    public MyAdapter(Context context, ArrayList<ClassUser> tracks) {
        super(context, 0, tracks);
    }

    /**
     * Parses the information from the array into the custom row layout.
     *
    @Override
    public View getView(int position, View view, ViewGroup group) {
        // Get the track's class at the current position
        MusicClass track = getItem(position);
        view = LayoutInflater.from(getContext())
                .inflate(R.layout.score_layout, group, false);

        // Get the views and put the appropriate values in it
        final TextView RankView     = view.findViewById(R.id.rankView);
        final TextView TrackView    = view.findViewById(R.id.trackView);
        final TextView ArtistView   = view.findViewById(R.id.artistView);
        final TextView YearView     = view.findViewById(R.id.yearView);
        final ImageButton favButton = view.findViewById(R.id.favView);
        final ImageView img         = view.findViewById(R.id.albumView);

        final String url            = track.getImageURL();
        Picasso.with(getContext()).load(url).into(img);

        RankView.setText(track.getRank());
        TrackView.setText(track.getTrack());
        ArtistView.setText(track.getArtist());
        YearView.setText(track.getYear());

        /**
         * This will add the appropriate instance of the Music Class to the favorites Hash Map
         * of the current user upon clicking the favorite button within the adapter listview.
         *
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    String userid = user.getUid();

                    fbdb = FirebaseDatabase.getInstance();
                    dbref = fbdb.getReference("User/user/" + userid);

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the values of the MusicClass at the clicked position
                            String album  = url;
                            String rank   = RankView.getText().toString();
                            String title  = TrackView.getText().toString();
                            String artist = ArtistView.getText().toString();
                            String year   = YearView.getText().toString();

                            MusicClass newTrack = new MusicClass(album, rank, title, artist, year);

                            // Update the user database by adding the newly favorited track
                            String userid = user.getUid();
                            fbdb = FirebaseDatabase.getInstance();
                            dbref = fbdb.getReference("User");

                            try {
                                dbref.child("user").child(userid).child("favorites").child(rank)
                                        .setValue(newTrack);
                                favButton.setImageResource(R.drawable.ic_check_black);
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("onCancelled MusicClass", databaseError.toString());
                        }
                    });
                    favButton.setBackgroundResource(R.drawable.ic_check);

                    // Let the user know that the item was added
                    Toast.makeText(getContext(),
                            "Toegevoegd aan favorieten", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the user that adding favorites when not logged in is not possible
                    Toast.makeText(getContext(),
                            "U bent niet ingelogd", Toast.LENGTH_SHORT).show();
                }
            }
        });
        notifyDataSetChanged();
        return view;
    }
    */
}