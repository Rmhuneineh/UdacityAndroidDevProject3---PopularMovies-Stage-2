package com.example.android.popmovies;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by rmhuneineh on 13/03/2018.
 */

public class MovieCursorAdapter extends CursorRecyclerAdapter<MovieCursorAdapter.ViewHolder> {

    private static final String base_path = "https://image.tmdb.org/t/p/w342";

    private static final String uri_key = "content_uri";
    private static final String favorites_key = "favorites";

    private MainActivity activity = new MainActivity();

    public MovieCursorAdapter(MainActivity context, Cursor c) {
        super(context, c);
        this.activity = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView posterImage;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.poster_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(FavoritesContract.FavoritesEntry._ID));
        int posterColumnIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.
                COLUMN_MOVIE_POSTER);

        String posterPath = cursor.getString(posterColumnIndex);

        Picasso.with(activity)
                .load(base_path + posterPath)
                .placeholder(R.drawable.fight_club_poster)
                .into(viewHolder.posterImage);

        viewHolder.posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                Uri currentFavoriteUri = ContentUris.withAppendedId(FavoritesContract
                        .FavoritesEntry.CONTENT_URI, id);
                intent.putExtra(uri_key, currentFavoriteUri.toString());
                if (activity.getSortOrder().equals(activity.getString(R.string.favorites))) {
                    intent.putExtra(favorites_key, activity.getSortOrder());
                }
                activity.startActivity(intent);
            }
        });
    }

}
