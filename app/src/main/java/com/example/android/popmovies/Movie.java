package com.example.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rmhuneineh on 07/03/2018.
 */

public class Movie implements Parcelable {

    String mTitle;
    String mReleaseDate;
    String mPosterPath;
    double mVoteAverage;
    String mPlotSynopsis;
    String mBackdropPath;

    public Movie(String title, String releaseDate, String posterPath, double voteAverage,
                 String plotSynopsis, String backdropPath) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
        mBackdropPath = backdropPath;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mPosterPath = in.readString();
        mVoteAverage = in.readFloat();
        mPlotSynopsis = in.readString();
        mBackdropPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // Getters
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

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    // Setters
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

    public void setPlotSynopsis(String plotSynopsis) {
        mPlotSynopsis = plotSynopsis;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mPosterPath);
        parcel.writeString(mBackdropPath);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mPlotSynopsis);
    }
}
