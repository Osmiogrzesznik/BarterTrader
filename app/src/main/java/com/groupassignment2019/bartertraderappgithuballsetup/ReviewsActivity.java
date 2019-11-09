package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.AddElementToListValueListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ReviewRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ReviewDataModel;

import java.util.ArrayList;

/**
 * expects extras:
 * uuid: id of user whose reviews should be shown;
 */
public class ReviewsActivity extends AppCompatActivity {
    //Listeners
    final ReviewRVAdapter.OnItemClickListener clickListener = new ReviewRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ReviewDataModel clickedReviewDataModel) {
            Toast.makeText(ReviewsActivity.this, "you clicked" + clickedReviewDataModel.getTitle(), Toast.LENGTH_LONG).show();
        }
    };

    private RecyclerView recyclerView;
    private DatabaseReference DB_mRootReference;
    private ArrayList<ReviewDataModel> reviewsArrayList;
    private LayoutInflater mInflater;
    private LinearLayoutManager linearLayoutManager;
    private ReviewRVAdapter reviewRVAdapter;
    private Intent intentThatStartedMe;
    private AddElementToListValueListener<ReviewDataModel> addItemToListOnValueEvent;
    private String UserId_WhoseReviewsWeSee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        recyclerView = findViewById(R.id.reviewsRecyclerView);

        //FIREBASE VARIABLES SET UP
        DB_mRootReference = FirebaseDatabase.getInstance().getReference();


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

        //this will happen for every review
        // TODO: 09/11/2019 this can be used rather for every userId in review to get some Pictures
        addItemToListOnValueEvent = new AddElementToListValueListener<ReviewDataModel>(this, reviewRVAdapter,ReviewDataModel.class);
            setUpAdapterToBeFilledWithReviewsOfUser();
    }


    private void setUpAdapterToBeFilledWithReviewsOfUser() {
        DB_mRootReference.child("users").child(UserId_WhoseReviewsWeSee).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reviewSnapshot: dataSnapshot.getChildren()){
                    ReviewDataModel reviewData = reviewSnapshot.getValue(ReviewDataModel.class);
                    reviewRVAdapter.add(reviewData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}