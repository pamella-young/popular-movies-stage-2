package com.example.android.popularmovies.Utilities;

import android.content.Context;

import com.example.android.popularmovies.Data.TrailerVideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pamella on 21-Jul-17.
 */

public class TrailerVideoJsonUtils {

    public static ArrayList<TrailerVideoData> getTrailerVideoDataFromJson(Context context, String trailerJsonStr)
            throws JSONException{

        final String TDJ_RESULTS = "results";
        final String TDJ_ID = "id";
        final String TDJ_KEY = "key";
        final String TDJ_NAME = "name";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONArray trailerDataArray = trailerJson.getJSONArray(TDJ_RESULTS);

        ArrayList<TrailerVideoData> trailerList = new ArrayList<TrailerVideoData>();

        for (int i=0; i<trailerDataArray.length(); i++){
            TrailerVideoData trailer = new TrailerVideoData();

            String id;
            String key;
            String name;

            JSONObject trailerDetails = trailerDataArray.getJSONObject(i);

            id = trailerDetails.getString(TDJ_ID);
            key = trailerDetails.getString(TDJ_KEY);
            name = trailerDetails.getString(TDJ_NAME);

            trailer.setId(id);
            trailer.setKey(key);
            trailer.setName(name);

            trailerList.add(trailer);
        }

        return trailerList;
    }
}
