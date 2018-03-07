package com.example.android.popmovies;

import android.content.Intent;
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
                .load(R.string.poster_base_path + posterPath)
                .placeholder(R.drawable.fight_club_poster)
                .into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(Integer.toString(R.string.intent_key), currentMovie);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_image);
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
