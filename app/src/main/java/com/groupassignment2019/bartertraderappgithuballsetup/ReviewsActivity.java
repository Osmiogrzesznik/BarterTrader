package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.IdentifiableValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.UserDataLoader;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.UserObserver;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ReviewRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ReviewDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * expects extras:
 * uuid: id of user whose reviews should be shown;
 */
public class ReviewsActivity extends AppCompatActivity  implements UserObserver {
    //Listeners
    final ReviewRVAdapter.OnItemClickListener clickListener = new ReviewRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ReviewDataModel clickedReviewDataModel) {
            Toast.makeText(ReviewsActivity.this, "you clicked" + clickedReviewDataModel.getTitle(), Toast.LENGTH_LONG).show();
        }
    };
    private CircleImageView viBtnAddReview;
    private RecyclerView recyclerView;
    private ArrayList<ReviewDataModel> reviewsArrayList;
    private LayoutInflater mInflater;
    private LinearLayoutManager linearLayoutManager;
    private ReviewRVAdapter reviewRVAdapter;
    private Intent intentThatStartedMe;
    private IdentifiableValueEventListener<ReviewDataModel> addItemToListOnValueEvent;
    private String UserId_WhoseReviewsWeSee;
    private UserDataModel me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        viBtnAddReview = findViewById(R.id.btnAddReview);
        recyclerView = findViewById(R.id.reviewsRecyclerView);

        reviewsArrayList = new ArrayList<>();

        //prepare RecyclerView
        mInflater = LayoutInflater.from(this.getBaseContext());
        reviewRVAdapter = new ReviewRVAdapter(mInflater, reviewsArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewRVAdapter);
        reviewRVAdapter.setOnItemClickListener(clickListener);

        //Recover info sent from previous activity
        intentThatStartedMe = getIntent();
        UserId_WhoseReviewsWeSee = intentThatStartedMe.getStringExtra("uuid");
        if (UserId_WhoseReviewsWeSee == null){
            Log.e("BOLO","no user id passed into reviews Activity with key name of uuid");
            throw new NullPointerException("no user id passed into reviews Activity with key name of uuid");
        }

        DB.Auth.addAuthStateListener(new UserDataLoader(this)); // require user to be logged in ? fishy this object

        //this will happen for every review
        // TODO: 09/11/2019 this can be used rather for every userId in review to get some Pictures
        DB.user_reviews
                .child(UserId_WhoseReviewsWeSee)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    Toast.makeText(ReviewsActivity.this, "no such user:"+UserId_WhoseReviewsWeSee , Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                reviewsArrayList.clear();
                for (DataSnapshot reviewSnapshot: dataSnapshot.getChildren()){
                    ReviewDataModel reviewData = reviewSnapshot.getValue(ReviewDataModel.class);
                    reviewsArrayList.add(reviewData);
                }
                reviewRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void addReview(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateUI(UserDataModel userDataModel) {
        me = userDataModel;
        if (me.getTradedWith().containsKey(UserId_WhoseReviewsWeSee)){
        viBtnAddReview.setVisibility(View.VISIBLE);
        }
    }
}


