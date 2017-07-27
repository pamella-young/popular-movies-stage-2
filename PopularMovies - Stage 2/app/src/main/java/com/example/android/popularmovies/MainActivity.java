package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.Data.MovieContract;
import com.example.android.popularmovies.Data.MovieData;
import com.example.android.popularmovies.Utilities.MovieJsonUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();

    Boolean mSortPopular = true;
    /**
     * mSortOptions
     * 1 = sort by popular
     * 2 = sort by highest rating
     * 3 = my favorite
     */
    private int mSortOptions = 1;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movies);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData();
    }

    /**
     * This code for isOnline() method is taken from stackoverflow (linked at the implementation Guide)
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnectedOrConnecting();
    }

    /**
     * method loadMovieData() is used to load the movie posters to the screen
     * if there's network connection, it calls the FetchMovieTask
     * if there's no network connection, it shows a toast
     */
    private void loadMovieData(){
        if(isOnline()){
            new FetchMovieTask().execute();
        }
        else{
            String toastMsg = getResources().getString(R.string.notOnline);
            mToast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<MovieData> cursorToArrayList(Cursor cursor){
        if(cursor != null) {
            ArrayList<MovieData> movieDatas = new ArrayList<MovieData>();

            int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int imagePathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_PATH);
            int synopsisIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS);
            int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
            int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

            cursor.moveToFirst();

            while (!cursor.moveToNext()) {
                MovieData movie = new MovieData();
                movie.setId(cursor.getString(idIndex));
                movie.setTitle(cursor.getString(titleIndex));
                movie.setReleaseDate(cursor.getString(releaseDateIndex));
                movie.setSynopsis(cursor.getString(synopsisIndex));
                movie.setRating(cursor.getString(ratingIndex));
                movie.setImagePath(cursor.getString(imagePathIndex));

                movieDatas.add(movie);

            }

            if(movieDatas != null){
                Log.v(TAG,movieDatas.get(1).getTitle());
            }
            return movieDatas;
        }
        else return null;
    }

    /**
     * This class fetch the data from the web and return it as ArrayList of MovieData objects
     */
    public class FetchMovieTask extends AsyncTask<Void, Void, Void>{

        ArrayList<MovieData> movieDatas = new ArrayList<MovieData>();

        @Override
        protected Void doInBackground(Void... params) {
            if(mSortOptions == 3){
                try {
                    Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null, null, null, null);
                    if(cursor == null){
                        Log.v(TAG,"failed to get from resolver");
                    }
                    movieDatas = cursorToArrayList(cursor);
                }catch (Exception e){
                    Log.e(TAG, "Failed to load data from resolver.");
                    e.printStackTrace();
                }
            }
            else{
                URL movieRequestUrl = NetworkUtils.buildUrl(mSortPopular);
                Log.v(TAG,"Fetch data from network");
                try{
                    String jsonMovieResponse = NetworkUtils.
                            getResponseFromHttpUrl(movieRequestUrl);

                    ArrayList<MovieData> simpleJsonMovieData = MovieJsonUtils
                            .getMovieDataFromJson(MainActivity.this, jsonMovieResponse);

                    movieDatas = simpleJsonMovieData;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mMovieAdapter.setMovieData(movieDatas);
        }
    }

    /**
     * The methods below are all related to menu
     * OnCreateOptionMenu inflate the menu
     * onOptionsItemSelected load the data based on user's choice of sorting preferences
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sort_by_popularity){
            mSortOptions = 1;
            mSortPopular = true;
            loadMovieData();
        }
        else if(id == R.id.sort_by_ranking){
            mSortOptions = 2;
            mSortPopular = false;
            loadMovieData();
        }
        else if(id == R.id.sort_by_favorite){
            mSortOptions = 3;
            loadMovieData();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method called when user click a movie poster
     * It creates an intent and send the clickedMovie object to the DetailActivity
     * @param clickedMovie = a MovieData object containing the clicked movie details
     */
    @Override
    public void onClick(MovieData clickedMovie) {
        Context context = this;
        Class detailClass = DetailActivity.class;
        Intent intent = new Intent(this, detailClass);

        intent.putExtra("movie_data", clickedMovie);

        startActivity(intent);
    }
}
