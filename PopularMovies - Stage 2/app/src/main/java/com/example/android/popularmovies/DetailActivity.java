package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.MovieContract;
import com.example.android.popularmovies.Data.MovieData;
import com.example.android.popularmovies.Data.ReviewData;
import com.example.android.popularmovies.Data.TrailerVideoData;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.Utilities.ReviewJsonUtils;
import com.example.android.popularmovies.Utilities.TrailerVideoJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerVideoAdapter.TrailerAdapterOnClickHandler{
    private static final String TAG = DetailActivity.class.getSimpleName();

    MovieData movie;
    Bundle data;
    TextView mTitleTextView;
    ImageView mPosterImageView;
    TextView mSynopsisTextView;
    TextView mReleaseDateTextView;
    TextView mRatingTextView;
    final String BASE_URL = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w342";
    final String BASE_VIDEO_URL = "http://youtube.com/watch?v=";
    String imagePath;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerVideoAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTrailerRecyclerView = (RecyclerView)findViewById(R.id.rv_trailer_videos);
        mReviewRecyclerView = (RecyclerView)findViewById(R.id.rv_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(layoutManager1);
        mReviewRecyclerView.setHasFixedSize(false);

        mTrailerAdapter = new TrailerVideoAdapter(DetailActivity.this, this);
        mReviewAdapter = new ReviewAdapter();
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        if(!intent.hasExtra("movie_data")){
            Toast.makeText(this,"Intent contains no data", Toast.LENGTH_LONG).show();
        }
        else{
            data = intent.getExtras();
        }
        movie = (MovieData) data.getParcelable("movie_data");

        setMovieDetails();

        loadTrailerAndReview();
    }

    public void onClickAddToFavorite(View view){

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_PATH, movie.getImagePath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if(uri!=null){
            Toast.makeText(getBaseContext(), uri.toString(),Toast.LENGTH_LONG).show();
        }

        finish();
    }

    private void loadTrailerAndReview(){
        new FetchTrailerAndReviewTask().execute();
    }

    public class FetchTrailerAndReviewTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            URL trailerRequestUrl = NetworkUtils.buildVideoUrl(movie.getId());
            URL reviewRequestUrl = NetworkUtils.buildReviewUrl(movie.getId());

            try{
                String jsonTrailerResponse =
                        NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                ArrayList<TrailerVideoData> simpleJsonTrailerData = TrailerVideoJsonUtils
                        .getTrailerVideoDataFromJson(DetailActivity.this, jsonTrailerResponse);

                String jsonReviewResponse =
                        NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                ArrayList<ReviewData> simpleJsonReviewData = ReviewJsonUtils
                        .getReviewDataFromJson(DetailActivity.this, jsonReviewResponse);

                if(simpleJsonTrailerData != null){
                    Log.v(TAG,"Trailer Data exists.");
                    mTrailerAdapter.setTrailerData(simpleJsonTrailerData);
                }
                if(simpleJsonReviewData != null){
                    Log.v(TAG,"Review Data exists.");
                    mReviewAdapter.setReviewData(simpleJsonReviewData);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void setMovieDetails(){
        mTitleTextView = (TextView) findViewById(R.id.tv_header);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);

        imagePath = BASE_URL + IMAGE_SIZE + movie.getImagePath();

        mTitleTextView.setText(movie.getTitle());
        Picasso.with(this).load(imagePath).into(mPosterImageView);
        mSynopsisTextView.setText(movie.getSynopsis());
        mReleaseDateTextView.setText(movie.getReleaseDate().substring(0,4));
        mRatingTextView.setText(movie.getRating());
    }


    @Override
    public void onClick(TrailerVideoData clickedTrailer) {
        String VIDEO_URL = BASE_VIDEO_URL + clickedTrailer.getKey();
        Uri videoUri = Uri.parse(VIDEO_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }
}
