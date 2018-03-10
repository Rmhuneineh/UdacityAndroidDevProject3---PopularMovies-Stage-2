package com.example.android.popmovies;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by rmhuneineh on 07/03/2018.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE_PATH = "movie";
    private static final String API_KEY_PARAM = "api_key";
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    public NetworkUtils() {
    }

    public static URL buildUrl(String sortOrder) {
        Log.v(LOG_TAG, "buildUrl Called!");

        Uri movieQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, "1b8d584a2753ce32f45181eb98e06bc9")
                .build();

        URL movieQueryUrl;
        try {
            movieQueryUrl = new URL(movieQueryUri.toString());
            return movieQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v(LOG_TAG, "getResponseFromHrrpUrl Called!");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;

            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;

        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<Movie> fetchMovies(URL requestedUrl) {
        Log.v(LOG_TAG, "fetchMovies Called!");

        String jsonResponse = null;

        try {
            jsonResponse = getResponseFromHttpUrl(requestedUrl);
        } catch (IOException e) {
            Log.e("NetworkUtils", "Problem making https request.", e);
        }
        List<Movie> movies = extractResultsFromJson(jsonResponse);
        return movies;
    }

    public static List<Movie> extractResultsFromJson(String movieJson) {
        Log.v(LOG_TAG, "extractResultsFromJson Called!");
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }

        List<Movie> movies = new ArrayList<Movie>();

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJson);
            JSONArray results = baseJsonResponse.getJSONArray(RESULTS);
            Log.v("NetworkUtils", "Array Size: " + results.length());
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
                String movieId = movieObject.getString(ID);
                String movieTitle = movieObject.getString(TITLE);
                String posterPath = movieObject.getString(POSTER_PATH);
                String backdropPath = movieObject.getString(BACKDROP_PATH);
                String plotSynopsis = movieObject.getString(OVERVIEW);
                double rating = movieObject.getDouble(VOTE_AVERAGE);
                String releaseDate = movieObject.getString(RELEASE_DATE);

                Movie movie = new Movie(movieId, movieTitle, releaseDate, posterPath, rating, plotSynopsis,
                        backdropPath);
                movies.add(movie);

            }

        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing the movieJSON results", e);
        }

        return movies;
    }

}
