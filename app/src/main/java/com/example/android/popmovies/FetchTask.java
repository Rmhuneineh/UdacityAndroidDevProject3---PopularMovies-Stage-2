package com.example.android.popmovies;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.net.URL;
import java.util.List;

/**
 * Created by rmhuneineh on 10/03/2018.
 */

public class FetchTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String LOG_TAG = "FetchTask";

    private MainActivity mContext;
    private MovieRecyclerAdapter mRecyclerAdapter;

    public FetchTask(MainActivity context, MovieRecyclerAdapter recyclerAdapter) {
        mContext = context;
        mRecyclerAdapter = recyclerAdapter;
    }

    @Override
    protected List<Movie> doInBackground(String... urls) {
        Log.v(LOG_TAG, "doInBackground Called!");
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }

        String sortOrder = urls[0];
        URL requestUrl = NetworkUtils.buildUrl(sortOrder);
        try {

            List<Movie> movieList = NetworkUtils.fetchMovies(requestUrl);
            mRecyclerAdapter.setMovies(movieList);
            return movieList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        Log.v(LOG_TAG, "onPostExecute Called!");
        super.onPostExecute(movies);

        mContext.mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerAdapter = new MovieRecyclerAdapter(mContext, movies);
        mContext.mRecyclerView.setAdapter(mRecyclerAdapter);
        mContext.mRecyclerView.setVisibility(View.VISIBLE);


    }

}
