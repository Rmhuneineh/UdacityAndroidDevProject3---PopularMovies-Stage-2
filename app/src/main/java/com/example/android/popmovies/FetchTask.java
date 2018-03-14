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

    private MainActivity mContext = null;
    private DetailsActivity mActivity = null;
    private MovieRecyclerAdapter mRecyclerAdapter = null;
    private String mId;
    private Movie mCurrentMovie;

    public FetchTask(MainActivity context, MovieRecyclerAdapter recyclerAdapter) {
        mContext = context;
        mRecyclerAdapter = recyclerAdapter;
    }

    public FetchTask(DetailsActivity activity, Movie currentMovie) {
        mActivity = activity;
        mId = currentMovie.getId();
        mCurrentMovie = currentMovie;
    }

    @Override
    protected List<Movie> doInBackground(String... urls) {
        Log.v(LOG_TAG, "doInBackground Called!");
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }

        if(urls.length == 1) {
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
        } else {
            URL videosUrl = NetworkUtils.buildVideosUrl(mId);
                try {
                    String youtubeKey = NetworkUtils.fetchVideos(videosUrl);
                    mCurrentMovie.setYoutubeKey(youtubeKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
            }

            URL reviewsUrl = NetworkUtils.buildReviewsUrl(mId);
            try {
                NetworkUtils.fetchReviews(reviewsUrl, mCurrentMovie);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        Log.v(LOG_TAG, "onPostExecute Called!");
        super.onPostExecute(movies);

        if(mContext!=null && mRecyclerAdapter!=null) {
            mContext.mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerAdapter = new MovieRecyclerAdapter(mContext, movies);
            mContext.mRecyclerView.setAdapter(mRecyclerAdapter);
            mContext.mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mActivity.reviewRecyclerAdapter = new ReviewRecyclerAdapter(mActivity, mCurrentMovie);
            mActivity.reviewRecyclerView.setAdapter(mActivity.reviewRecyclerAdapter);
            mActivity.setUI();
            mActivity.getSupportLoaderManager().initLoader(mActivity.EXISTING_FAVORITES_LOADER,
                    mActivity.bundle, mActivity);
        }


    }

}
