package com.example.android.popularmovies.Utilities;

import android.content.Context;

import com.example.android.popularmovies.Data.ReviewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pamella on 24-Jul-17.
 */

public class ReviewJsonUtils {

    private static final String TAG = ReviewJsonUtils.class.getSimpleName();
    public static ArrayList<ReviewData> getReviewDataFromJson(Context context, String reviewJsonStr)
            throws JSONException {


        final String RDJ_RESULTS = "results";
        final String RDJ_AUTHOR = "author";
        final String RDJ_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        JSONArray reviewDataArray = reviewJson.getJSONArray(RDJ_RESULTS);

        ArrayList<ReviewData> reviewList = new ArrayList<ReviewData>();

        for (int i=0; i<reviewDataArray.length(); i++){
            ReviewData review = new ReviewData();

            String author;
            String content;

            JSONObject reviewDetails = reviewDataArray.getJSONObject(i);

            author = reviewDetails.getString(RDJ_AUTHOR);
            content = reviewDetails.getString(RDJ_CONTENT);

            review.setAuthor(author);
            review.setContent(content);
            //Log.v(TAG,"review content: " + review.getContent());
            reviewList.add(review);
        }

        return reviewList;
    }
}
