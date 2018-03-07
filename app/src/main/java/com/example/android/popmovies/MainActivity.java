package com.example.android.popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

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
                R.integer.column_number);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new MovieRecyclerAdapter(this, mMoviesList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        setMovies();

        if(hasConnection()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mErrorTV.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mErrorTV.setText(R.string.error_message);
            mErrorTV.setVisibility(View.VISIBLE);
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

    private void setMovies() {
        Movie movie = new Movie(String.valueOf(R.string.title_example),
                String.valueOf(R.string.release_date_example),
                "/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg", 8.4,
                String.valueOf(R.string.overview_example),
                "/mMZRKb3NVo5ZeSPEIaNW9buLWQ0.jpg");
        for(int i=0; i<20; i++) {
            mMoviesList.add(movie);
        }
    }
}
