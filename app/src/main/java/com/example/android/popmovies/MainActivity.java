package com.example.android.popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.error_tv)
    TextView mErrorTV;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    ArrayList<Movie> mMoviesList = new ArrayList<Movie>();
    MovieRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(LOG_TAG, "OnCreate Called!");

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.column_number));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new MovieRecyclerAdapter(this, mMoviesList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        if(hasConnection()) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorTV.setVisibility(View.GONE);

            String sortOrder = getResources().getString(R.string.default_value);

            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(sortOrder);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mErrorTV.setText(R.string.error_message);
            mErrorTV.setVisibility(View.VISIBLE);
        }
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

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

            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerAdapter = new MovieRecyclerAdapter(MainActivity.this, movies);
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);


        }

    }

    private Boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean  isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
