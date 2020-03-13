package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.IdentifiableValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ReviewRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ReviewDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * expects extras:
 * uuid: id of user whose reviews should be shown;
 */
public class ReviewsActivity extends AppCompatActivity {
    private static final int NEW_REVIEW = 9210;
    //Listeners
    private final ReviewRVAdapter.OnItemClickListener clickListener = new ReviewRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ReviewDataModel clickedReviewDataModel) {
            Toast.makeText(ReviewsActivity.this, "you clicked" + clickedReviewDataModel.getTitle(), Toast.LENGTH_LONG).show();
        }
    };
    private CircleImageView ivLeftmostimgToolbar;
    private TextView tvMainTextToolbar;
    //other
    private TextView tvIfEmpty;
    private CircleImageView ivBtnAddReview;
    private ArrayList<ReviewDataModel> reviewsArrayList;
    private ReviewRVAdapter reviewRVAdapter;
    private IdentifiableValueEventListener<ReviewDataModel> addItemToListOnValueEvent;
    private String yourID;
    private UserDataModel me;
    private UserDataModel you;
    private boolean reviewWasWrittenAndAverageHasToBeRecalculated = false;
    private ReviewDataModel newReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        tvIfEmpty = findViewById(R.id.tv_ifEmpty);

        //Toolbar UI
        //toolbar
        Toolbar barterBar = findViewById(R.id.barterBar);
        ivLeftmostimgToolbar = barterBar.findViewById(R.id.iv_leftMostImg_toolbar);
        CircleImageView ivProfilepicToolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        ImageView ivInboxIconToolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
        TextView tvUnreadThreadsAmountCircle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        tvMainTextToolbar = barterBar.findViewById(R.id.tv_mainText_toolbar);
        ivBtnAddReview = findViewById(R.id.iv_AddReview);
        RecyclerView recyclerView = findViewById(R.id.reviewsRecyclerView);

        reviewsArrayList = new ArrayList<>();

        //prepare RecyclerView

        LayoutInflater mInflater = LayoutInflater.from(this.getBaseContext());
        reviewRVAdapter = new ReviewRVAdapter(mInflater, reviewsArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewRVAdapter);
        reviewRVAdapter.setOnItemClickListener(clickListener);

        //Recover info sent from previous activity
        Intent intentThatStartedMe = getIntent();
        yourID = intentThatStartedMe.getStringExtra("uuid");
        if (yourID == null) {
            Log.e("BOLO", "no user id passed into reviews Activity with key name of uuid");
            throw new NullPointerException("no user id passed into reviews Activity with key name of uuid");
        }

        FirebaseUser AuthMe = FirebaseAuth.getInstance().getCurrentUser();

        DB.users.child(AuthMe.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDataModel user = dataSnapshot.getValue(UserDataModel.class);
                        Log.d("BOLO", user.toString());
                        me = user;
                        updateUI();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ReviewsActivity.this, "could not load ", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //TODO pictures of authors ?
        DB.users.child(yourID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot user_youDS) {
                        if (user_youDS.exists()) {
                            you = user_youDS.getValue(UserDataModel.class);
                            updateUI();
                        } else {
                            //never should happen really (only admins can delete account on request after reviewing no crime has been commited);
                            Toast.makeText(ReviewsActivity.this, "Sorry User has Just deleted his account", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        DB.user_reviews
                .child(yourID)
                .addValueEventListener(keepUpdatingReviewsListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DB.user_reviews
                .child(yourID)
                .removeEventListener(keepUpdatingReviewsListAdapter);
    }

    public void addReview(View view) {
        Intent intent = new Intent(this, WriteNewReviewActivity.class);
        intent.putExtra("yourID", yourID);
        startActivityForResult(intent, NEW_REVIEW);
    }


    private void updateUI() {

        if (me.getTradedWith().containsKey(yourID)) { //allow to write review only after trading
            ivBtnAddReview.setVisibility(View.VISIBLE);
        }

        if (you != null){
        tvMainTextToolbar.setText(you.getFullName());
            if (you.getPicture() != null) {
                Picasso.get().load(you.getPicture()).into(ivLeftmostimgToolbar);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == NEW_REVIEW && resultCode == RESULT_OK) {
            newReview = (ReviewDataModel) data.getSerializableExtra("newReview");
            reviewWasWrittenAndAverageHasToBeRecalculated = true;
        }

    }


    private ValueEventListener keepUpdatingReviewsListAdapter = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot reviewsListDS) {
            if (!reviewsListDS.exists()) {
                //happens if no reviews had been written yet
                tvIfEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(ReviewsActivity.this, "this user have no reviews yet be first:" + yourID, Toast.LENGTH_LONG).show();
                return;
            }

            tvIfEmpty.setVisibility(View.GONE);
            reviewsArrayList.clear();
            for (DataSnapshot reviewDS : reviewsListDS.getChildren()) {
                ReviewDataModel reviewData = reviewDS.getValue(ReviewDataModel.class);
                reviewsArrayList.add(reviewData);
            }
            reviewRVAdapter.notifyDataSetChanged();
            //only if review has just been written
            if (reviewWasWrittenAndAverageHasToBeRecalculated && newReview != null) {
                int amountOfReviews = reviewsArrayList.size();
                int sumOfReviewsRatings = 0;
                for (ReviewDataModel review : reviewsArrayList) {
                    sumOfReviewsRatings = sumOfReviewsRatings + review.getRating();
                    //if newly added review is our review
                    if (review.keyID != null && review.keyID.equals(newReview.keyID)) {

                        /**
                         * as there is no possibility to remove a review
                         * the newly added one will be always the last one
                         *
                         *                         calculateAverage
                         * this way data will be consistent without using transaction because
                         * every user currently observing reviewsList
                         * will run this code only if he wrote last new review
                         *
                         *amountOfReviews will never be zero because this code wouldnt run if there would be no elements
                         */

                        int avgOfReviews = Math.round(sumOfReviewsRatings / amountOfReviews);

                        HashMap<String, Object> mapUpdate = new HashMap();
                        mapUpdate.put("/avgRev/", avgOfReviews);
                        mapUpdate.put("/amtRev/", amountOfReviews);
                        DB.users.child(yourID).updateChildren(mapUpdate); // update seller average and amount of reviews
                        //reset to allow another review
                        reviewWasWrittenAndAverageHasToBeRecalculated = false;
                        newReview = null; // can be done safely because newly added review is different object
                    }
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}


