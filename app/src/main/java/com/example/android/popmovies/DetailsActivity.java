package com.example.android.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DetailsActivity";

    private static final String base_path = "https://image.tmdb.org/t/p/w342";

    private static final String TITLE_KEY = "title_key";
    private static final String RELEASE_DATE_KEY = "realease_date_key";
    private static final String VOTE_AVERAGE_KEY = "vote_average_key";
    private static final String OVERVIEW_KEY = "overview_key";
    private static final String BACKDROP_PATH_KEY = "backdrop_path_key";

    private Boolean favorite = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setUI(bundle);


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
