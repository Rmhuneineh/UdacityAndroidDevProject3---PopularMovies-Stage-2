package com.example.android.popmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rmhuneineh on 13/03/2018.
 */

public class FavoritesProvider extends ContentProvider {

    private FavoritesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE_ID:
                selection = FavoritesContract.FavoritesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return FavoritesContract.FavoritesEntry.CONTENT_LIST_TYPE;
            case FAVORITE_ID:
                return FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        String idString = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        if (idString == null) {
            throw new IllegalArgumentException("Movie requires ID");
        }

        String title = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE);
        if (title == null ) {
            throw new IllegalArgumentException("Movie requires title");
        }

        String date = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_DATE);
        if (date == null) {
            throw new IllegalArgumentException("Movie requires release date");
        }

        String poster = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER);
        if(poster == null) {
            throw new IllegalArgumentException("Movie requires poster path");
        }

        String rating = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING);
        if(rating == null) {
            throw new IllegalArgumentException("Movie requires rating");
        }

        String overview = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW);
        if(overview == null) {
            throw new IllegalArgumentException("Movie requires overview");
        }

        String backdrop = values.getAsString(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP);
        if (backdrop == null) {
            throw new IllegalArgumentException("Movie requires backdrop path");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(FavoritesContract.FavoritesEntry.TABLE_NAME,
                null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;


        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                rowsDeleted = database.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        null, null);
                break;
            case FAVORITE_ID:
                selection = FavoritesContract.FavoritesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                       @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static final int FAVORITES = 100;

    private static final int FAVORITE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_FAVORITES,
                FAVORITES);
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY,
                FavoritesContract.PATH_FAVORITES + "/#", FAVORITE_ID);
    }
}
