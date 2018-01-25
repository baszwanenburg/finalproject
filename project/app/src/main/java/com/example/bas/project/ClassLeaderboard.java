package com.example.bas.project;

/**
 * Created by Bas on 11-1-2018.
 */

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contains the user information
 */
public class ClassLeaderboard implements Serializable {
    public String username;
    public String highscore;
    public String date;
    public String time;
    public String speed;

    // Default constructor for Firebase
    public ClassLeaderboard() {}

    public ClassLeaderboard(String username, String highscore, String date, String time, String speed) {
        this.username  = username;
        this.highscore = highscore;
        this.date      = date;
        this.time      = time;
        this.speed     = speed;
    }
}