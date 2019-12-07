package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.UserDataLoader;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

public class MyProfileActivity extends AppCompatActivity {
    private UserDataModel me;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView tv_dashboard_welcome_txt;
    private UserDataModel user;
    private Toolbar barterBar;
    private TextView tv_unreadThreadsAmount_circle;
    private ImageView iv_profilepic_toolbar;
    private ImageView iv_BigProfilePic_dashboard;
    private ImageView iv_inbox_icon_toolbar;
    private RatingBar ratingBar;
    private TextView tvAmountOfReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        barterBar = findViewById(R.id.BarterBar);
//        btnLogout = findViewById(R.id.btn_logout_dashboard);
//        btnMyProfileDashboard = findViewById(R.id.btn_myProfile_dashboard);
//        btnSearchCategoriesDashboard = findViewById(R.id.btn_searchCategories_dashboard);
//        btnPostNewItemDashboard = findViewById(R.id.btn_postNewItem_dashboard);
//        btnInboxDashboard = findViewById(R.id.btn_inbox_dashboard);
//        tv_dashboard_welcome_txt = findViewById(R.id.tv_dashboard_welcome_txt);
//        iv_BigProfilePic_dashboard = findViewById(R.id.iv_BigProfilePic_dashboard);
//        tv_unreadThreadsAmount_circle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
//        iv_profilepic_toolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
//        iv_inbox_icon_toolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
//        ratingBar = findViewById(R.id.ratingBar_itemDetail);//clickable
//        tvAmountOfReviews = findViewById(R.id.tv_amountOfReviews_itemDetails);//clickable


//        tv_unreadThreadsAmount_circle.setVisibility(View.GONE);

//        iv_inbox_icon_toolbar.setOnClickListener(openInboxActivity);
//
//        DB.Auth.addAuthStateListener(new UserDataLoader(this));
//
//        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//           deleteAccount();
//
//            }
//        });

        //stuff like in registration but editing



    }

    private void deleteAccount() {
       // ask for confirmation
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(this, LoginActivity.class);
        intToMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intToMain);
        finish();
    }


    public void updateUI(UserDataModel me) {
        if (me == null) {
            finish();
        }

        user = me;
        tv_dashboard_welcome_txt.setText("Welcome " + user.getFirstName() + "!");
        ratingBar.setRating(me.getAvgRev());
        tvAmountOfReviews.setText(String.valueOf(me.getAmtRev()));
        //tvSellerHasBeenFlaggedIndicator.setVisibility(me.isFlagged() ? View.VISIBLE : View.GONE);
        //Picasso.get().load(me.getPicture()).into(ivSellerPicItemDetail);


        int unreadThreadsAmount = user.calculateUnreadThreadAmount();
        if (unreadThreadsAmount > 0) {
            tv_unreadThreadsAmount_circle.setVisibility(View.VISIBLE);
            tv_unreadThreadsAmount_circle.setText(String.valueOf(unreadThreadsAmount));
        } else {
            tv_unreadThreadsAmount_circle.setVisibility(View.GONE);
        }
        String picURl = user.getPicture();
        if (picURl != null) {
            Picasso.get().load(picURl).placeholder(R.drawable.avvy).into(iv_BigProfilePic_dashboard);
            Picasso.get().load(picURl).placeholder(R.drawable.avvy).into(iv_profilepic_toolbar);
        }


    }

    public void changeprofilepic(View view) {
    }

    public void GO_TO_activity_reviews(View view) {
    }
}
