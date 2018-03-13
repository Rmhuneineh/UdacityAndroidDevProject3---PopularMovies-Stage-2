package com.example.android.popmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rmhuneineh on 13/03/2018.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favorites.db";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME +
                "(" +
                FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " TEXT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_DATE + " TEXT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER + " TEXT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING + " TEXT, " +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP + " TEXT);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
