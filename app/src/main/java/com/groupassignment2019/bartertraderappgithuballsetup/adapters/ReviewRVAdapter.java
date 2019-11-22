package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ReviewDataModel;

import java.util.List;


public class ReviewRVAdapter extends RecyclerView.Adapter<ReviewRVAdapter.ReviewDataViewHolder> implements Consumer<ReviewDataModel> {
    private OnItemClickListener onItemClickListener;
    private List<ReviewDataModel> reviews;
    private LayoutInflater mInflater;

    public interface OnItemClickListener {
        void onItemClick(ReviewDataModel clickedReviewDataModel);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ReviewRVAdapter( LayoutInflater mInflater, List<ReviewDataModel> reviews) {
        this.reviews = reviews;
        this.mInflater = mInflater;
    }

    public void updateItems(List<ReviewDataModel> newList){
        this.reviews = newList;
        notifyDataSetChanged();
    }

    public void consume(ReviewDataModel reviewData){
        this.reviews.add(reviewData);
        Log.d("BOLO",reviewData.toString());
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ReviewDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View reviewView = mInflater
                .inflate(R.layout.cardview_review, parent, false);
        return new ReviewDataViewHolder(reviewView);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewDataViewHolder holder, int position) {
        ReviewDataModel review = reviews.get(position);
        holder.textViewReviewTitle.setText(review.getTitle());
        holder.reviewBody.setText(review.getBody());
        holder.reviewCard_RatingBar.setRating(review.getRating());

// TODO: 09/11/2019 picture of the reviewer ?
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    // View holder

    /**
     * View holder
     */
    public class ReviewDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View reviewCardParentWrapper;
        private final TextView textViewReviewTitle;
        private final TextView reviewBody;
        private final RatingBar reviewCard_RatingBar;

        public ReviewDataViewHolder(@NonNull View cardview_reviewView) {
            super(cardview_reviewView);
            reviewCardParentWrapper = cardview_reviewView.findViewById(R.id.reviewCardParentWrapper);
            textViewReviewTitle = cardview_reviewView.findViewById(R.id.textViewReviewTitle);
            reviewBody = cardview_reviewView.findViewById(R.id.reviewBody);
            reviewCard_RatingBar = cardview_reviewView.findViewById(R.id.reviewCard_RatingBar);

            cardview_reviewView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(reviews.get(position));
                }
            }
        }
    }




}

