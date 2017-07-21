package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Data.TrailerVideoData;

import java.util.ArrayList;

/**
 * Created by Pamella on 21-Jul-17.
 */

public class TrailerVideoAdapter extends RecyclerView.Adapter<TrailerVideoAdapter.TrailerVideoAdapterViewHolder> {
    private static final String TAG = TrailerVideoAdapter.class.getSimpleName();

    private ArrayList<TrailerVideoData> mTrailerData;
    final private TrailerAdapterOnClickHandler mOnClickListener;

    Context myContext;

    public interface TrailerAdapterOnClickHandler{
        void onClick(TrailerVideoData clickedTrailer);
    }

    public TrailerVideoAdapter(Context myContext, TrailerAdapterOnClickHandler listener){
        this.myContext = myContext;
        mOnClickListener = listener;
    }

    class TrailerVideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mPlayButtonTextView;
        public final TextView mTrailerTitleTextView;

        public TrailerVideoAdapterViewHolder(View itemView) {
            super(itemView);
            mPlayButtonTextView = (TextView) itemView.findViewById(R.id.tv_video_play_button);
            mTrailerTitleTextView = (TextView) itemView.findViewById(R.id.tv_video_title);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            mTrailerTitleTextView.setText("Trailer " + String.valueOf(listIndex));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            TrailerVideoData clickedTrailer = mTrailerData.get(clickedPosition);
            mOnClickListener.onClick(clickedTrailer);
        }
    }

    @Override
    public TrailerVideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerVideoAdapterViewHolder(view);
    }

    public void onBindViewHolder(TrailerVideoAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mTrailerData == null) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData(ArrayList<TrailerVideoData> trailerData){
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
