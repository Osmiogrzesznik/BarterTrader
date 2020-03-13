package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ReviewDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class WriteNewReviewActivity extends AppCompatActivity {

    private CircleImageView ivLeftmostimgToolbar;
    private TextView tvMainTextToolbar;

    //other
    private String yourID;
    private UserDataModel you;
    private ReviewDataModel newReview;
    private CircleImageView ivLeftMostImgToolbar;
    private RatingBar ratingBarItemDetail;
    private EditText etBody;
    private EditText etTitle;
    private Button postReviewButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_review);
        //Toolbar UI
        //toolbar
        Toolbar barterBar = findViewById(R.id.BarterBar);
        ivLeftmostimgToolbar = barterBar.findViewById(R.id.iv_leftMostImg_toolbar);
        CircleImageView ivProfilepicToolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        ImageView ivInboxIconToolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
        TextView tvUnreadThreadsAmountCircle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        tvMainTextToolbar = barterBar.findViewById(R.id.tv_mainText_toolbar);

        ratingBarItemDetail = findViewById(R.id.ratingBar_newReview);
        etBody = findViewById(R.id.editTextDescription);
        etTitle = findViewById(R.id.editTextTitle);
        postReviewButton = findViewById(R.id.PostItemButton);
        progressBar = findViewById(R.id.progressBar);

        //these are not needed here
        ivProfilepicToolbar.setVisibility(View.GONE);
        ivInboxIconToolbar.setVisibility(View.GONE);
        tvUnreadThreadsAmountCircle.setVisibility(View.GONE);

        String myID = DB.me.getUid();

        Intent startingIntent = getIntent();
        yourID = startingIntent.getStringExtra("yourID");
        if (yourID == null) {
            throw new NullPointerException("New Review started without required yourID string extra");
        }

        DB.users.child(yourID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(@NonNull DataSnapshot user_youDS) {
                                                                      if (user_youDS.exists()) {
                                                                          you = user_youDS.getValue(UserDataModel.class);
                                                                          updateUI();
                                                                      } else {
                                                                          //never should happen really (only admins can delete account on request after reviewing no crime has been commited);
                                                                          Toast.makeText(WriteNewReviewActivity.this, "Sorry User has Just deleted his account", Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                  }
                                                              }
        );


    }

    private void updateUI() {
        if (you.getPicture() != null) {
            Picasso.get().load(you.getPicture()).into(ivLeftmostimgToolbar);
        }
        tvMainTextToolbar.setText(you.getFullName());
    }

    public void setReviewRating(View view) {
    }



    private void validate_SaveReview_To_DB() {

        InputValidator iv = new InputValidator(this);

        boolean ok = iv.notEmpty(etTitle) && iv.notEmpty(etBody) && ratingBarItemDetail.getRating() > 0;

        if (!ok) {
            //display the toast , additionally InputValidator class will take care of pointing where the errors are
            Toast.makeText(this, "complete the required data: rating, description and title", Toast.LENGTH_LONG).show();
            return;
        }

        String newReviewIdKey = DB.user_reviews.push().getKey();

        newReview = new ReviewDataModel();
        newReview.keyID = newReviewIdKey;
        newReview.setTitle(etTitle.getText().toString());
        newReview.setBody(etBody.getText().toString());
        newReview.setAuthorUID(DB.myUid());
        newReview.setRating((int) ratingBarItemDetail.getRating());
        newReview.setReceiverUID(yourID);

        progressBar.setVisibility(View.VISIBLE);
        postReviewButton.setEnabled(false);

        Log.d("BOLO", "about to save to db");
        // TODO: 08/12/2019 calculate average and use map from chatactivity


        DB.user_reviews.
                child(yourID).
                child(newReviewIdKey).
                setValue(newReview).
                addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    progressBar.setVisibility(View.GONE);
                                    //it was called for result
                                    Intent intent = new Intent();
                                    intent.putExtra("newReview", newReview);
                                    setResult(RESULT_OK, intent);
                                    finish(); // will redirect to ReviewsActivity in background
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    postReviewButton.setEnabled(true);
                                    Toast.makeText(WriteNewReviewActivity.this, "Problem occured please try again later", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
    }

    public void postReview(View view) {
        validate_SaveReview_To_DB();
    }
}
