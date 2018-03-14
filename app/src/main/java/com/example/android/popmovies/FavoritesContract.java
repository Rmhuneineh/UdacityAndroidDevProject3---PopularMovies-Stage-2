package com.example.android.popmovies;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rmhuneineh on 13/03/2018.
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static abstract class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_POSTER = "posterPath";
        public static final String COLUMN_MOVIE_RATING = "voteAverage";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_BACKDROP = "backdropPath";
        public static final String COLUMN_MOVIE_FAVORITE = "favorite";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

    }

}
