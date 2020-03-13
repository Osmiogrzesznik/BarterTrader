package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.UserObserver;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements UserObserver {
    private static final int PICK_IMAGE = 55;
    private UserDataModel me;
    FirebaseAuth mAuth;

//    private TextView tv_dashboard_welcome_txt;
    private TextView tv_unreadThreadsAmount_circle;
    private ImageView iv_profilepic_toolbar;
//    private ImageView iv_BigProfilePic_dashboard;
    private ImageView iv_inbox_icon_toolbar;
//    private TextView tvAmountOfReviews;
//    private RatingBar ratingBar;
    private UserDataModel user;
    private Toolbar barterBar;
    private TextView tvFullName;
    private TextView barterLevelTitle;
    private TextView bartAmount;
    private TextView textView2;
    private Button btnMyProfileDashboard;
    private CircleImageView ivBigProfilePicDashboard;
    private CircleImageView ivLeftMostImgToolbar;
    private CircleImageView ivProfilepicToolbar;
    private ImageView ivInboxIconToolbar;
    private TextView tvUnreadThreadsAmountCircle;
    private TextView tvMainTextToolbar;
    private RatingBar ratingBarItemDetail;
    private TextView tvAmountOfReviewsItemDetails;
    private TextView tvSellerReviewsLinkItemDetail;
    private Button btnFlagUser;
    private Uri imageLocalURI;
    private ProgressBar progressBar2;

    private String newItemImageFirebaseFilename;
    private Uri uploadedImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        barterBar = findViewById(R.id.BarterBar);


        DB.getMeReference().addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDataModel user = dataSnapshot.getValue(UserDataModel.class);
                        Log.d("BOLO", user.toString());
                        updateUI(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MyProfileActivity.this, "could not load " , Toast.LENGTH_SHORT).show();
                    }
                }
        );

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        tvFullName = (TextView) findViewById(R.id.tv_dashboard_welcome_txt);
        barterLevelTitle = (TextView) findViewById(R.id.barterLevelTitle);
        bartAmount = (TextView) findViewById(R.id.bartAmount);
        textView2 = (TextView) findViewById(R.id.textView2);
        btnMyProfileDashboard = (Button) findViewById(R.id.btn_myProfile_dashboard);
        ivBigProfilePicDashboard = (CircleImageView) findViewById(R.id.iv_BigProfilePic_dashboard);
        ivLeftMostImgToolbar = (CircleImageView) findViewById(R.id.iv_leftMostImg_toolbar);
        ivProfilepicToolbar = (CircleImageView) findViewById(R.id.iv_profilepic_toolbar);
        ivInboxIconToolbar = (ImageView) findViewById(R.id.iv_inbox_icon_toolbar);
        tvUnreadThreadsAmountCircle = (TextView) findViewById(R.id.tv_unreadThreadsAmount_circle);
        tvMainTextToolbar = (TextView) findViewById(R.id.tv_mainText_toolbar);
        ratingBarItemDetail = (RatingBar) findViewById(R.id.ratingBar_newReview);
        tvAmountOfReviewsItemDetails = (TextView) findViewById(R.id.tv_amountOfReviews_itemDetails);
        tvSellerReviewsLinkItemDetail = (TextView) findViewById(R.id.tv_sellerReviews_Link_itemDetail);
        btnFlagUser = (Button) findViewById(R.id.btn_flag_user);



//        btnLogout = findViewById(R.id.btn_logout_dashboard);
//        btnMyProfileDashboard = findViewById(R.id.btn_myProfile_dashboard);
//        btnSearchCategoriesDashboard = findViewById(R.id.btn_searchCategories_dashboard);
//        btnPostNewItemDashboard = findViewById(R.id.btn_postNewItem_dashboard);
//        btnInboxDashboard = findViewById(R.id.btn_inbox_dashboard);
//        tv_dashboard_welcome_txt = findViewById(R.id.tv_dashboard_welcome_txt);
//        iv_BigProfilePic_dashboard = findViewById(R.id.iv_BigProfilePic_dashboard);
        tv_unreadThreadsAmount_circle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        iv_profilepic_toolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        iv_inbox_icon_toolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
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
            Toast.makeText(this, "No such user in db", Toast.LENGTH_SHORT).show();
            finish();
        }

        user = me;
        tvFullName.setText(user.getFullName());
        ratingBarItemDetail.setRating(user.getAvgRev());
        tvAmountOfReviewsItemDetails.setText(String.valueOf(user.getAmtRev()));



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
            Picasso.get().load(picURl).placeholder(R.drawable.avvy).into(ivBigProfilePicDashboard);
            Picasso.get().load(picURl).placeholder(R.drawable.avvy).into(iv_profilepic_toolbar);
        }
    }

    private void uploadImageFileAndCheckIfAllCompleted() {
        progressBar2.setVisibility(View.VISIBLE);
        final StorageReference uploadRef = FirebaseStorage.getInstance()
                .getReference("user_images") // path contains appropriate folder
                .child(newItemImageFirebaseFilename); // filename
        uploadRef.putFile(imageLocalURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Log.d("BOLO","success uploading image file");
                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("BOLO","success getting url of image file");
                        // TODO: 04/12/2019 Picture is not saved ?!
                        // todo make it dead simple  - two separate ones , array of files to upload didnt work
                        if (uri == null){
                            throw new NullPointerException("image added but firebase send null back ?!!!!");
                        }
                        //fileToUpload.setDownloadUri(uri);
                        uploadedImageURL = uri;
                        DB.getMeReference().child("picture").setValue(uploadedImageURL.toString());
                        Picasso.get().load(imageLocalURI).into(ivBigProfilePicDashboard);
                        progressBar2.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MyProfileActivity.this, " failed to upload the picture - cancelled", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfileActivity.this, " failed to upload the picture", Toast.LENGTH_SHORT).show();
                //should delete any item references from DB
            }
        });
    }



    private String getFileExtension(Uri uri){
        return "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void changeprofilepic(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void GO_TO_activity_reviews(View view) {
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "you heave to select some picture, sorry", Toast.LENGTH_LONG).show();
                return;
            }
            if (data == null || data.getData() == null) {
                Toast.makeText(this, "data is nulll   you heave to select some picture, sorry", Toast.LENGTH_LONG).show();
                return;
            }
            imageLocalURI = data.getData();
            newItemImageFirebaseFilename = DB.myUid() + getFileExtension(imageLocalURI);

            // filesToUpload = new ArrayList<>();
            //FileToUpload imageFile = new FileToUpload(imageLocalURI, newItemImageFirebaseFilename, "user_images");
            uploadImageFileAndCheckIfAllCompleted();
        }
    }



}
