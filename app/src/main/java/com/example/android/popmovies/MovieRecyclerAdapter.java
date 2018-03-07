package com.example.android.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rmhuneineh on 07/03/2018.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    private static final String LOG_TAG = "MovieRecyclerAdapter";

    private static final String base_path = "https://image.tmdb.org/t/p/w342";

    private static final String TITLE_KEY = "title_key";
    private static final String RELEASE_DATE_KEY = "realease_date_key";
    private static final String VOTE_AVERAGE_KEY = "vote_average_key";
    private static final String OVERVIEW_KEY = "overview_key";
    private static final String BACKDROP_PATH_KEY = "backdrop_path_key";





    List<Movie> mMovies;
    MainActivity mContext;

    @Override
    public MovieRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View gridItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);

        ViewHolder viewHolder = new ViewHolder(gridItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie currentMovie = mMovies.get(position);
        String posterPath = currentMovie.getPosterPath();

        Picasso.with(mContext)
                .load(base_path + posterPath)
                .placeholder(R.drawable.fight_club_poster)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_image);

            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                    Movie chosenMovie = mMovies.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString(TITLE_KEY, chosenMovie.getTitle());
                    bundle.putString(RELEASE_DATE_KEY, chosenMovie.getReleaseDate());
                    bundle.putDouble(VOTE_AVERAGE_KEY, chosenMovie.getVoteAverage());
                    bundle.putString(OVERVIEW_KEY, chosenMovie.getOverview());
                    bundle.putString(BACKDROP_PATH_KEY, chosenMovie.getBackdropPath());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public MovieRecyclerAdapter(MainActivity context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }

}
