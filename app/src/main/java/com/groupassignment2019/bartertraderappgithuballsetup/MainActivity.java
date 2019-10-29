package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //when you create your activity uncomment the appropriate code that redirects to it,
        // if your activity has different name, rename it by pressing CTRL + F6 on the file name(recommended)
        // , or rename class inside functions below (not recommended for clarity)
    }

    public void GO_TO_activity_Welcome(View view) {
//        Intent intent = new Intent(this, WelcomeActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Login(View view) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Register(View view) {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Dashboard(View view) {
//        Intent intent = new Intent(this, DashboardActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_MyProfile(View view) {
//        Intent intent = new Intent(this, MyProfileActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_PostNewItem(View view) {
//        Intent intent = new Intent(this, PostNewItemActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Categories(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_ItemDetails(View view) {
//        Intent intent = new Intent(this, ItemDetailsActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_SellerProfile(View view) {
//        Intent intent = new Intent(this, SellerProfileActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Chat(View view) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Inbox(View view) {
//        Intent intent = new Intent(this, InboxActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_SellerReviews(View view) {
//        Intent intent = new Intent(this, SellerReviewsActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_MyReviews(View view) {
//        Intent intent = new Intent(this, MyReviewsActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_ItemsByMe(View view) {
//        Intent intent = new Intent(this, ItemsByMeActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_ItemsByCategory(View view) {
//        Intent intent = new Intent(this, ItemsByCategoryActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_ItemsBySeller(View view) {
//        Intent intent = new Intent(this, ItemsBySellerActivity.class);
//        startActivity(intent);
    }

    public void GOT_TO_WatchItemVideoActivity(View view) {
//        Intent intent = new Intent(this, WatchItemVideoActivity.class);
//        startActivity(intent);
    }
}
