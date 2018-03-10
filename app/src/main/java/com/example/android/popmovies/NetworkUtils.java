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
    private static final String VIDEOS_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";
    private static final String API_KEY_PARAM = "api_key";
    private static final String RESULTS = "results";
    private static final String KEY = "key";
    private static final String ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String api_key = "1b8d584a2753ce32f45181eb98e06bc9";

    public NetworkUtils() {
    }

    public static URL buildVideosUrl(String id) {
        Log.v(LOG_TAG, "buildVideosUrl Called!");

        Uri videoQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(id)
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        URL videoQueryUrl;
        try {
            videoQueryUrl = new URL(videoQueryUri.toString());
            return videoQueryUrl;
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildReviewsUrl(String id) {
        Log.v(LOG_TAG, "buildVideosUrl Called!");

        Uri reviewQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(id)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        URL reviewQueryUrl;
        try {
            reviewQueryUrl = new URL(reviewQueryUri.toString());
            return reviewQueryUrl;
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildUrl(String sortOrder) {
        Log.v(LOG_TAG, "buildUrl Called!");

        Uri movieQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, api_key)
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

    public static String fetchVideos(URL videosUrl) {
        Log.v(LOG_TAG, "fetchVideos Called!");

        String videosJsonResponse = null;
        try {
            videosJsonResponse = getResponseFromHttpUrl(videosUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String youtubeKey = extractKeyFromJson(videosJsonResponse);
        return youtubeKey;
    }

    public static void fetchReviews(URL reviewsUrl, Movie currentMovie) {
        Log.v(LOG_TAG, "fetchReviews Called!");

        String reviewsJsonResponse = null;
        try {
            reviewsJsonResponse = getResponseFromHttpUrl(reviewsUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractReviewsFromJson(reviewsJsonResponse, currentMovie);
    }

    public static void extractReviewsFromJson(String reviewsJson, Movie currentMovie) {
        Log.v(LOG_TAG, "extractReviewsFromJson Called!");
        if(TextUtils.isEmpty(reviewsJson)) {
            return;
        }

        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(reviewsJson);
            JSONArray results = baseJsonResponse.getJSONArray(RESULTS);
            if(results.length()>0) {
                for(int i=0; i<results.length(); i++) {
                   JSONObject review = results.getJSONObject(i);
                   authors.add(review.getString(AUTHOR));
                   contents.add(review.getString(CONTENT));
                   Log.v(LOG_TAG, contents.get(i) + "\n" + authors.get(i));
                }

            } else {
                currentMovie.setAuthors(null);
                currentMovie.setContents(null);
            }
            currentMovie.setAuthors(authors);
            currentMovie.setContents(contents);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String extractKeyFromJson(String videosJson) {
        Log.v(LOG_TAG, "extractKeyFromJson Called!");
        if(TextUtils.isEmpty(videosJson)) {
            return null;
        }

        String youtubeKey = null;

        try {
            JSONObject baseJsonResponse = new JSONObject(videosJson);
            JSONArray results = baseJsonResponse.getJSONArray(RESULTS);
            JSONObject item = results.getJSONObject(0);
            youtubeKey = item.getString(KEY);
            Log.v(LOG_TAG, youtubeKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return youtubeKey;
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
