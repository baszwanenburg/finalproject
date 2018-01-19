package com.example.bas.project;

/**
 * Created by Bas on 11-1-2018.
 */
public class ClassMovie {
    private String id;
    private String image;
    private String title;
    private String overview;
    private String year;
    private String review;

    public ClassMovie(String id, String image, String title, String overview, String year, String review) {
        this.id       = id;
        this.image    = image;
        this.title    = title;
        this.overview = overview;
        this.year     = year;
        this.review   = review;
    }

    // Default constructor for FireBase
    public ClassMovie() {}

    String getID() {
        return id;
    }

    String getImage() {
        return image;
    }

    String getMovieTitle() {
        return title;
    }

    String getOverview() {
        return overview;
    }

    String getYear() {
        return year;
    }

    String getReview() {return review;}
}