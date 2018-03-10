package com.example.android.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DetailsActivity";

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

    @BindView(R.id.details_scroll_view)
    ScrollView scrollView;

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

    @BindView(R.id.reviews_frame)
    FrameLayout reviewsFrameLayout;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewRecyclerView;

    ReviewRecyclerAdapter reviewRecyclerAdapter;

    Boolean hiddenRecyclerView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        reviewRecyclerView.setVisibility(View.GONE);


        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        setUI(bundle);

        extractBundle(bundle);

        FetchTask videoAndReviewsTask = new FetchTask(currentMovie);
        videoAndReviewsTask.execute("videos", "reviews");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(this, currentMovie);
        reviewRecyclerView.setAdapter(reviewRecyclerAdapter);

        trailerFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(YOUTUBE_BASE_PATH + currentMovie.getYoutubeKey()));
                startActivity(intent);
            }
        });

        reviewsFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentMovie.getAuthors().size() != 0){
                    if(!hiddenRecyclerView) {
                        reviewRecyclerView.setVisibility(View.GONE);
                        hiddenRecyclerView = true;
                    } else {
                        reviewRecyclerView.setVisibility(View.VISIBLE);
                        hiddenRecyclerView = false;
                    }
                } else {
                    Toast.makeText(DetailsActivity.this,
                            "Movie Doesn't Have Reviews =(", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void extractBundle(Bundle bundle) {
        currentMovie = new Movie(bundle.getString(ID_KEY), bundle.getString(TITLE_KEY),
                bundle.getString(RELEASE_DATE_KEY), bundle.getString(POSTER_KEY),
                bundle.getDouble(VOTE_AVERAGE_KEY), bundle.getString(OVERVIEW_KEY),
                bundle.getString(BACKDROP_PATH_KEY));
    }

    public void setUI(Bundle bundle) {
        Picasso.with(this)
                .load(base_path + bundle.getString(BACKDROP_PATH_KEY))
                .placeholder(R.drawable.fight_club_backdrop)
                .into(backdropIV);

        titleTV.setText(bundle.getString(TITLE_KEY));
        releaseDateTV.setText(bundle.getString(RELEASE_DATE_KEY));
        ratingTV.setText(String.valueOf(bundle.getDouble(VOTE_AVERAGE_KEY)));
        overviewTV.setText(bundle.getString(OVERVIEW_KEY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu Called!");
        getMenuInflater().inflate(R.menu.details_menu, menu);
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
                } else {
                    item.setIcon(R.drawable.favorite);
                    favorite = true;
            }

                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
