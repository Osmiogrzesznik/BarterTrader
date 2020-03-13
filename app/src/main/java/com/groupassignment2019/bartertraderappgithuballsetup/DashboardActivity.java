package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.UserObserver;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity implements UserObserver {
    Button btnLogout;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView tv_dashboard_welcome_txt;
    private UserDataModel user;
    private Toolbar barterBar;
    private TextView tv_unreadThreadsAmount_circle;
    private ImageView iv_profilepic_toolbar;
    private ImageView iv_BigProfilePic_dashboard;
    private ImageView iv_inbox_icon_toolbar;
    private Button btnMyProfileDashboard;
    private Button btnSearchCategoriesDashboard;
    private Button btnPostNewItemDashboard;
    private Button btnInboxDashboard;


    private View.OnClickListener openInboxActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GO_TO_inbox(v);
        }
    };

    private View.OnClickListener openMyProfileActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(DashboardActivity.this, "Not implemented yet", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DashboardActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener openCategoriesActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DashboardActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }
    };
    private RatingBar ratingBar;
    private TextView tvAmountOfReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        barterBar = findViewById(R.id.BarterBar);
        btnLogout = findViewById(R.id.btn_logout_dashboard);
        btnMyProfileDashboard = findViewById(R.id.btn_myProfile_dashboard);
        btnSearchCategoriesDashboard = findViewById(R.id.btn_searchCategories_dashboard);
        btnPostNewItemDashboard = findViewById(R.id.btn_postNewItem_dashboard);
        btnInboxDashboard = findViewById(R.id.btn_inbox_dashboard);
        tv_dashboard_welcome_txt = findViewById(R.id.tv_dashboard_welcome_txt);
        iv_BigProfilePic_dashboard = findViewById(R.id.iv_BigProfilePic_dashboard);
        tv_unreadThreadsAmount_circle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        iv_profilepic_toolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        iv_inbox_icon_toolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
        ratingBar = findViewById(R.id.ratingBar_newReview);//clickable
        tvAmountOfReviews = findViewById(R.id.tv_amountOfReviews_itemDetails);//clickable


        tv_unreadThreadsAmount_circle.setVisibility(View.GONE);

        iv_inbox_icon_toolbar.setOnClickListener(openInboxActivity);

        FirebaseUser AuthMe = FirebaseAuth.getInstance().getCurrentUser();

        DB.users.child(AuthMe.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDataModel user = dataSnapshot.getValue(UserDataModel.class);
                        Log.d("BOLO", user.toString());
                        updateUI(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DashboardActivity.this, "could not load ", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(DashboardActivity.this, LoginActivity.class);
                intToMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intToMain);
                DashboardActivity.this.finish();
            }
        });

    }

    /**
     * main task here is to update UI on any changes in user data - new unread inbox items, changes in picture and so on
     * @param me
     */
    @Override
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

    public void GO_TO_inbox(View view) {
        Intent intent = new Intent(this, InboxActivity.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        mAuth.signOut();
        finish();
    }

    public void GO_TO_searchCategories(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void GO_TO_PostNewItem(View view) {
        Intent intent = new Intent(this, PostNewItemActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_MyProfile(View view) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_reviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("uuid", DB.myUid());
        startActivity(intent);
    }

    public void GO_TO_activity_ItemsByMe(View view) {
        Intent intent = new Intent(this, ItemsListActivity.class);
        ItemsListActivity.BY_ME(intent); // contract enforcing static method
        startActivity(intent);
    }
}
