package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "MainActivity";

    private static final int FAVORITES_LOADER = 0;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.error_tv)
    TextView mErrorTV;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    ArrayList<Movie> mMoviesList = new ArrayList<Movie>();
    MovieRecyclerAdapter mRecyclerAdapter;
    MovieCursorAdapter mMovieCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(LOG_TAG, "OnCreate Called!");

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.column_number));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        loadMovies();
    }

    private void loadMovies() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String sortOrderKey = getString(R.string.key);
        String sortOrderDefault = getString(R.string.default_value);
        String sortOrder = sharedPreferences.getString(sortOrderKey, sortOrderDefault);

        switch (sortOrder) {
            case "popular":
            case "top_rated":
                mRecyclerAdapter = new MovieRecyclerAdapter(this, mMoviesList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                emptyView.setVisibility(View.GONE);

                if(hasConnection()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mErrorTV.setVisibility(View.GONE);

                    FetchTask movieTask = new FetchTask(this, mRecyclerAdapter);
                    movieTask.execute(sortOrder);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mErrorTV.setText(R.string.error_message);
                    mErrorTV.setVisibility(View.VISIBLE);
                }
                break;

            case "favorites":
                mMovieCursorAdapter = new MovieCursorAdapter(this, null);
                mRecyclerView.setAdapter(mMovieCursorAdapter);
                getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FavoritesContract.FavoritesEntry._ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_DATE,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_FAVORITE};

        return new CursorLoader(this,
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        mMovieCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu Called!");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "onOptionsItemSelected Called!");
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean  isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadMovies();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMovies();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
