package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Data.MovieData;
import com.example.android.popularmovies.Data.TrailerVideoData;
import com.example.android.popularmovies.Utilities.NetworkUtils;
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

    private RecyclerView mRecyclerView;
    private TrailerVideoAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_trailer_videos);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerVideoAdapter(DetailActivity.this, this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        Intent intent = getIntent();
        if(!intent.hasExtra("movie_data")){
            Toast.makeText(this,"Intent contains no data", Toast.LENGTH_LONG).show();
        }
        else{
            data = intent.getExtras();
        }
        movie = (MovieData) data.getParcelable("movie_data");

        setMovieDetails();

        loadTrailerData();
    }

    private void loadTrailerData(){
        new FetchTrailerTask().execute();
    }

    public class FetchTrailerTask extends AsyncTask<Void, Void, ArrayList>{
        @Override
        protected ArrayList doInBackground(Void... params) {
            URL trailerRequestUrl = NetworkUtils.buildVideoUrl(movie.getId());

            try{
                String jsonTrailerResponse =
                        NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                ArrayList<TrailerVideoData> simpleJsonTrailerData = TrailerVideoJsonUtils
                        .getTrailerVideoDataFromJson(DetailActivity.this, jsonTrailerResponse);

                return simpleJsonTrailerData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList trailerData) {
            if(trailerData != null){
                mTrailerAdapter.setTrailerData(trailerData);
            }
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
        Uri videoUri = Uri.parse(BASE_VIDEO_URL).buildUpon()
                .appendPath(clickedTrailer.getKey())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }
}
