package com.example.android.popmovies;

import java.util.ArrayList;

/**
 * Created by rmhuneineh on 07/03/2018.
 */

public class Movie {

    private static final String LOG_TAG = "Movie";

    String mId;
    String mTitle;
    String mReleaseDate;
    String mPosterPath;
    double mVoteAverage;
    String mOverview;
    String mBackdropPath;
    String mYoutubeKey;
    ArrayList<String> mAuthors = new ArrayList<>();
    ArrayList<String> mContents = new ArrayList<>();

    public Movie(String id, String title, String releaseDate, String posterPath, double voteAverage,
                 String overview, String backdropPath) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mBackdropPath = backdropPath;
    }

    // Getters
    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public String getYoutubeKey() {
        return mYoutubeKey;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public ArrayList<String> getContents() {
        return mContents;
    }

    // Setters
    public void setId(String id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public void setOverview(String plotSynopsis) {
        mOverview = plotSynopsis;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public void setYoutubeKey(String key) {
        mYoutubeKey = key;
    }


    public void setAuthors(ArrayList<String> authors) {
        mAuthors = authors;
    }

    public void setContents(ArrayList<String> contents) {
        mContents = contents;
    }
}
