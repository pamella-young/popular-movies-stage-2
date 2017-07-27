package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Data.ReviewData;

import java.util.ArrayList;

/**
 * Created by Pamella on 24-Jul-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<ReviewData> mReviewData;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mContentTextView;
        public final TextView mAuthorTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mAuthorTextView.setText(mReviewData.get(position).getAuthor());
        holder.mContentTextView.setText(mReviewData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviewData == null){
            Log.v(TAG, "Review Data doesn't exist.");
            return 0;
        }

        Log.v(TAG,"number of item : " + String.valueOf(mReviewData.size()));
        return mReviewData.size();
    }

    public void setReviewData(ArrayList<ReviewData> reviewData){
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
