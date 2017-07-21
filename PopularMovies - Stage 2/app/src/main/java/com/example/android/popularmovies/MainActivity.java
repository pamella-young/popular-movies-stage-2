package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.Data.MovieData;
import com.example.android.popularmovies.Utilities.MovieJsonUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    Boolean mSortPopular = true;

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

    /**
     * This class fetch the data from the web and return it as ArrayList of MovieData objects
     */
    public class FetchMovieTask extends AsyncTask<Void, Void, ArrayList>{

        @Override
        protected ArrayList<MovieData> doInBackground(Void... params) {
            URL movieRequestUrl = NetworkUtils.buildUrl(mSortPopular);

            try{
                String jsonMovieResponse = NetworkUtils.
                        getResponseFromHttpUrl(movieRequestUrl);

                ArrayList<MovieData> simpleJsonMovieData = MovieJsonUtils
                        .getMovieDataFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList movieData) {
            if(movieData != null){
                mMovieAdapter.setMovieData(movieData);
            }
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
            mSortPopular = true;
            loadMovieData();
        }
        else if(id == R.id.sort_by_ranking){
            mSortPopular = false;
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
