package com.example.bas.project;

import java.io.Serializable;

/**
 * Contains the data to be stored in the leaderboards.
 */
public class ClassLeaderboard implements Serializable {
    public String username;
    public String date;
    public String time;
    public String speed;
    int score;

    // Default constructor for Firebase
    public ClassLeaderboard() {}

    public ClassLeaderboard(String username, int score, String date, String time, String speed) {
        this.username = username;
        this.score    = score;
        this.date     = date;
        this.time     = time;
        this.speed    = speed;
    }

    String getUsername() {
        return username;
    }

    int getScore() {
        return score;
    }

    String getDate() {
        return date;
    }

    String getTime() {
        return time;
    }

    String getSpeed() {
        return speed;
    }
}