package com.example.android.popmovies;

import android.util.Log;

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

    public Movie(String id, String title, String releaseDate, String posterPath, double voteAverage,
                 String overview, String backdropPath) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mBackdropPath = backdropPath;

        Log.v(LOG_TAG, mTitle + "\n" + mReleaseDate + "\n" + mVoteAverage + "\n" + mOverview);
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
}
