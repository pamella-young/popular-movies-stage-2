package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.Data.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pamella on 26-Jun-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    final String BASE_URL = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w500";
    final private MovieAdapterOnClickHandler mOnClickListener;

    Context myContext;

    /**
     * Interface for ClickHandler
     */
    public interface MovieAdapterOnClickHandler{
        void onClick(MovieData clickedMovie);
    }

    /**
     * Constructor
     * @param myContext
     * @param listener
     */
    public MovieAdapter(Context myContext, MovieAdapterOnClickHandler listener) {
        this.myContext = myContext;
        mOnClickListener = listener;
    }

    private ArrayList<MovieData> mMovieData;

    /**
     * MovieAdapterViewHolder class
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.imageview_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            MovieData clickedMovie = mMovieData.get(clickedPosition);
            mOnClickListener.onClick(clickedMovie);
        }
    }

    /**
     * These methods below are all related to the viewholder
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * onBindViewHolder to bind the data
     * @param holder
     * @param position the position of the image clicked
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String imagePath = BASE_URL + IMAGE_SIZE + mMovieData.get(position).getImagePath();
        Picasso.with(myContext).load(imagePath).into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if(mMovieData == null) return 0;
        return mMovieData.size();
    }

    public void setMovieData(ArrayList<MovieData> movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
