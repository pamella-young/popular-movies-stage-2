package com.example.android.popularmovies.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Pamella on 25-Jun-17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_URL = "https://api.themoviedb.org/3/movie/";

    private static final String MOVIE_BASE_URL = MOVIE_DB_URL;

    private static final String API_PARAM = "api_key";
    private static final String LANG_PARAM = "language";
    private static final String INCLUDE_ADULT_PARAM = "include_adult";
    private static final String INCLUDE_VIDEO_PARAM = "include_video";
    private static final String PAGE_PARAM = "page";

    //insert api_key in the apiKey variable
    //I tried to use save the api key in build.gradle but it shows error
    private static final String apiKey = "";
    private static final String lang = "en-US";
    private static final String sortByPopular = "popular";
    private static final String sortByRating = "top_rated";
    private static String sort = "";

    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    /**
     * This method is to build the Url
     * @param sortPopular (Boolean): default = true, true if user choose to sort by popularity
     *                               false = if user choose to sort by highest rating
     * @return URL address
     */
    public static URL buildUrl(Boolean sortPopular){
        if(sortPopular)sort = sortByPopular;
        else sort = sortByRating;
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(API_PARAM, apiKey)
                .appendQueryParameter(LANG_PARAM, lang)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, "false")
                .appendQueryParameter(INCLUDE_VIDEO_PARAM, "false")
                .appendQueryParameter(PAGE_PARAM, "1")
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildVideoUrl(String movieId){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_PARAM,apiKey)
                .appendQueryParameter(LANG_PARAM,lang)
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built Video URI " + url);

        return url;
    }

    public static URL buildReviewUrl(String movieId){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(REVIEWS)
                .appendQueryParameter(API_PARAM,apiKey)
                .appendQueryParameter(LANG_PARAM,lang)
                .appendQueryParameter(PAGE_PARAM,"1")
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built Review URI " + url);

        return url;
    }

    /**
     * This method is taken from the Exercise given within the course of Networking
     * Link: https://github.com/udacity/ud851-Sunshine
     * Link: https://github.com/udacity/ud851-Exercises
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
