package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
                getResources().getInteger(R.integer.column_number));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new MovieRecyclerAdapter(this, mMoviesList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        if(hasConnection()) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorTV.setVisibility(View.GONE);

            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this);

            String sortOrderKey = getString(R.string.key);
            String sortOrderDefault = getString(R.string.default_value);
            String sortOrder = sharedPreferences.getString(sortOrderKey, sortOrderDefault);

            FetchTask movieTask = new FetchTask(this, mRecyclerAdapter);
            movieTask.execute(sortOrder);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mErrorTV.setText(R.string.error_message);
            mErrorTV.setVisibility(View.VISIBLE);
        }
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
}
