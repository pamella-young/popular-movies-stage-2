package com.example.android.popularmovies.Utilities;

import android.content.Context;

import com.example.android.popularmovies.Data.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pamella on 26-Jun-17.
 */

public class MovieJsonUtils {
    /**
     * this method parses JSON from a web response and returns an arraylist of MovieData Objects
     *
     * @param movieJsonStr JSON response from server
     *
     * @return ArrayList of MovieData Object
     *
     */

    public static ArrayList<MovieData> getMovieDataFromJson(Context context, String movieJsonStr)
            throws JSONException{

        final String MDJ_RESULTS = "results";
        final String MDJ_ID = "id";
        final String MDJ_DESCRIPTION = "overview";
        final String MDJ_RATING = "vote_average";
        final String MDJ_TITLE = "title";
        final String MDJ_IMAGE = "poster_path";
        final String MDJ_RELEASEDATE = "release_date";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieDataArray = movieJson.getJSONArray(MDJ_RESULTS);

        ArrayList<MovieData> movieList = new ArrayList<MovieData>();

        for(int i=0; i< movieDataArray.length(); i++){
            MovieData movie = new MovieData();
            String id;
            String title;
            String imagePath;
            String synopsis;
            String rating;
            String releaseDate;

            JSONObject movieDetails = movieDataArray.getJSONObject(i);

            id = movieDetails.getString(MDJ_ID);
            title = movieDetails.getString(MDJ_TITLE);
            rating = movieDetails.getString(MDJ_RATING);
            imagePath = movieDetails.getString(MDJ_IMAGE);
            synopsis = movieDetails.getString(MDJ_DESCRIPTION);
            releaseDate = movieDetails.getString(MDJ_RELEASEDATE);

            movie.setId(id);
            movie.setTitle(title);
            movie.setRating(rating);
            movie.setImagePath(imagePath);
            movie.setSynopsis(synopsis);
            movie.setReleaseDate(releaseDate);

            movieList.add(movie);
        }

        return movieList;
    }
}
