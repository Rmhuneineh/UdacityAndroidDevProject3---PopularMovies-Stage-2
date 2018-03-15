package com.example.android.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "DetailsActivity";

    static final int EXISTING_FAVORITES_LOADER = 0;

    private static final String SAVED_STATE = "saved_state";

    private static final String uri_key = "content_uri";

    private static final String base_path = "https://image.tmdb.org/t/p/w342";
    private static final String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?v=";
    private static final String ID_KEY = "id_key";
    private static final String TITLE_KEY = "title_key";
    private static final String RELEASE_DATE_KEY = "realease_date_key";
    private static final String POSTER_KEY = "poster_key";
    private static final String VOTE_AVERAGE_KEY = "vote_average_key";
    private static final String OVERVIEW_KEY = "overview_key";
    private static final String BACKDROP_PATH_KEY = "backdrop_path_key";

    private Boolean favorite = false;
    private Movie currentMovie;
    private String mCurrentFavoriteUri = null;

    @BindView(R.id.details_scroll_view)
    ScrollView detailsSV;

    @BindView(R.id.backdrop)
    ImageView backdropIV;

    @BindView(R.id.title_tv)
    TextView titleTV;

    @BindView(R.id.release_date_tv)
    TextView releaseDateTV;

    @BindView(R.id.rating)
    TextView ratingTV;

    @BindView(R.id.overview_tv)
    TextView overviewTV;

    @BindView(R.id.trailer_frame)
    FrameLayout trailerFrameLayout;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewRecyclerView;

    ReviewRecyclerAdapter reviewRecyclerAdapter;
    Bundle bundle = new Bundle();

    Boolean favoritesDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.v(LOG_TAG, "onCreate Called!");

        ButterKnife.bind(this);
        Intent intent = getIntent();

        if(savedInstanceState == null) {
            if(intent.hasExtra("favorites")) {
                favoritesDetails = true;
            }

            if(intent.hasExtra(uri_key)) {
                mCurrentFavoriteUri = intent.getStringExtra(uri_key);
                getSupportLoaderManager().initLoader(EXISTING_FAVORITES_LOADER, null, this);
            } else {
                bundle = intent.getExtras();
                extractBundle(bundle);

                FetchTask videoAndReviewsTask = new FetchTask(this, currentMovie);
                videoAndReviewsTask.execute("videos", "reviews");
            }
        } else {
            if (savedInstanceState.containsKey(SAVED_STATE)) {
                bundle = savedInstanceState.getBundle(SAVED_STATE);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);

        trailerFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(YOUTUBE_BASE_PATH + currentMovie.getYoutubeKey()));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "onSaveInstanceState Called!");
        super.onSaveInstanceState(outState);
        outState.putBoolean("favorite", favorite);
        outState.putBundle(SAVED_STATE, bundle);
        outState.putParcelable("adapter", reviewRecyclerAdapter);
        outState.putString("uri", mCurrentFavoriteUri);
        outState.putBoolean("favorites", favoritesDetails);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onRestoreInstanceState Called!");
        super.onRestoreInstanceState(savedInstanceState);
        extractBundle(savedInstanceState.getBundle(SAVED_STATE));
        setUI();
        reviewRecyclerAdapter = savedInstanceState.getParcelable("adapter");
        reviewRecyclerView.setAdapter(reviewRecyclerAdapter);
        favorite = savedInstanceState.getBoolean("favorite");
        mCurrentFavoriteUri = savedInstanceState.getString("uri");
        favoritesDetails = savedInstanceState.getBoolean("favorites");
        invalidateOptionsMenu();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader Called!");
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

        if (args == null) {
            return new CursorLoader(this,
                    Uri.parse(mCurrentFavoriteUri),
                    projection,
                    null,
                    null,
                    null);
        } else {
            Log.v(LOG_TAG, "args != null!");
            return new CursorLoader(this,
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "onLoadFinished Called!");
        if (cursor == null || cursor.getCount() < 1) {
            favorite = false;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        else if (mCurrentFavoriteUri != null) {
            // Find the columns of product attributes that we're interested in
            cursor.moveToLast();
            int idCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
            int titleCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE);
            int dateCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_DATE);
            int posterCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER);
            int ratingCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING);
            int overviewCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW);
            int backdropCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP);
            int isFavCI = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_FAVORITE);
            // Extract out the value from the Cursor for the given column index
            String id = cursor.getString(idCI);
            String title = cursor.getString(titleCI);
            String date = cursor.getString(dateCI);
            String poster = cursor.getString(posterCI);
            String rating = cursor.getString(ratingCI);
            String overview = cursor.getString(overviewCI);
            String backdrop = cursor.getString(backdropCI);
            int isFav = cursor.getInt(isFavCI);

            currentMovie = new Movie(id, title, date, poster, Double.valueOf(rating),
                    overview, backdrop);

            bundle.putString(ID_KEY, currentMovie.getId());
            bundle.putString(TITLE_KEY, currentMovie.getTitle());
            bundle.putString(RELEASE_DATE_KEY, currentMovie.getReleaseDate());
            bundle.putString(POSTER_KEY, currentMovie.getPosterPath());
            bundle.putDouble(VOTE_AVERAGE_KEY, currentMovie.getVoteAverage());
            bundle.putString(OVERVIEW_KEY, currentMovie.getOverview());
            bundle.putString(BACKDROP_PATH_KEY, currentMovie.getBackdropPath());

            if (isFav == 1) {
                favorite = true;
                currentMovie.setIsFav(favorite);

            } else {
                favorite = false;
                currentMovie.setIsFav(favorite);
            }


            FetchTask videoAndReviewsTask = new FetchTask(this, currentMovie);
            videoAndReviewsTask.execute("videos", "reviews");
        } else {
            boolean found = false;
            Log.v(LOG_TAG, String.valueOf(cursor.getCount()));
            for(int i=0; i<cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if(currentMovie.getId().equals(cursor.getString(cursor.getColumnIndex(FavoritesContract.
                        FavoritesEntry.COLUMN_MOVIE_ID)))) {
                    found = true;
                    mCurrentFavoriteUri = String.valueOf(cursor.getNotificationUri());
                }
            }
            favorite = found;
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "onLoaderReset Called!");
    }

    public void extractBundle(Bundle bundle) {
        Log.v(LOG_TAG, "extractBundle Called!");
        currentMovie = new Movie(bundle.getString(ID_KEY), bundle.getString(TITLE_KEY),
                bundle.getString(RELEASE_DATE_KEY), bundle.getString(POSTER_KEY),
                bundle.getDouble(VOTE_AVERAGE_KEY), bundle.getString(OVERVIEW_KEY),
                bundle.getString(BACKDROP_PATH_KEY));
    }

    public void setUI() {
        Log.v(LOG_TAG, "setUI Called!");
        Picasso.with(this)
                .load(base_path + currentMovie.getPosterPath())
                .placeholder(R.drawable.fight_club_backdrop)
                .into(backdropIV);

        titleTV.setText(currentMovie.getTitle());
        releaseDateTV.setText(currentMovie.getReleaseDate());
        ratingTV.setText(String.valueOf(currentMovie.getVoteAverage()));
        overviewTV.setText(currentMovie.getOverview());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu Called!");

        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onPrepareOptionsMenu Called!");

        super.onPrepareOptionsMenu(menu);
        if (favorite) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.favorite);
        } else {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.unfavorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "onOptionsItemSelected Called!");
        int id = item.getItemId();

        switch(id){
            case R.id.action_favorite:
                if(favorite) {
                    item.setIcon(R.drawable.unfavorite);
                    favorite = false;
                    unfavMovie();
                    if(favoritesDetails) {
                        finish();
                    }

                } else {
                    item.setIcon(R.drawable.favorite);
                    favorite = true;
                    favMovie();
            }
                currentMovie.setIsFav(favorite);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favMovie() {
        Log.v(LOG_TAG, "favMovie Called!");

        ContentValues values = new ContentValues();
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, currentMovie.getId());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE, currentMovie.getTitle());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_DATE, currentMovie.getReleaseDate());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER, currentMovie.getPosterPath());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING, String.valueOf(currentMovie.getVoteAverage()));
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW, currentMovie.getOverview());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP, currentMovie.getBackdropPath());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_FAVORITE, 1);


        mCurrentFavoriteUri = String.valueOf(getContentResolver()
                .insert(FavoritesContract.FavoritesEntry.CONTENT_URI, values));
        if (mCurrentFavoriteUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Error Saving!",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Saved to Favorites!",
                    Toast.LENGTH_SHORT).show();
            currentMovie.setIsFav(true);
        }
        Log.v(LOG_TAG, "favMovie Done!");
    }

    private void unfavMovie() {
        Log.v(LOG_TAG, "unfavMovie Called!");

        int rowsDeleted = getContentResolver().delete(Uri.parse(mCurrentFavoriteUri), null, null);
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "Failed to Unfavorite",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, "Removed From Favorites Successfully",
                    Toast.LENGTH_SHORT).show();
            currentMovie.setIsFav(false);
            mCurrentFavoriteUri = null;
        }
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "onPause Called!");
        super.onPause();

    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "onResume Called!");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
