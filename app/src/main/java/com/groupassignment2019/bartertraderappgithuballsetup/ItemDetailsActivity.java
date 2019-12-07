package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageThreadDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * expects extra Serializable itemData
 */
public class ItemDetailsActivity extends AppCompatActivity {

    private static final int PICK_ITEM_FOR_OFFER = 42;
    public static final String EXTRA_ITEM_KEY = "item";
    private ImageView ivImageItemDetail;
    private TextView tvItemTitleItemDetail;
    private RatingBar ratingBarItemDetail;
    private TextView tvSellerFullNameItemDetail;
    private CircleImageView ivSellerPicItemDetail;
    private TextView tvSellerHasBeenFlaggedIndicator;
    private TextView tvOtherUserItemsLinkItemDetail;
    private TextView tvAmountOfReviewsItemDetails;
    private TextView tvItemDescriptionItemDetail;
    private Button btnSeeItemVideoOptItemDetail;
    private Button btnMakeOfferToSellerItemDetail;
    private Button btnMsgSellerItemDetail;
    private ItemData item;
    private UserDataModel seller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 17/11/2019 this activity should display current user toolbar
        setContentView(R.layout.activity_item_details);
        Intent intentThatStartedThisActivity = getIntent();
        if (!intentThatStartedThisActivity.hasExtra(EXTRA_ITEM_KEY)) {
            String msg = "ItemDetailsActivity expects an ItemData item extra passed in intent";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            Log.e("BOLO", msg);
            finish();
        }
        item = (ItemData) intentThatStartedThisActivity.getSerializableExtra(EXTRA_ITEM_KEY);

        //item data display
        ivImageItemDetail = findViewById(R.id.iv_Image_itemDetail);
        tvItemTitleItemDetail = findViewById(R.id.tv_itemTitle_itemDetail);
        tvItemDescriptionItemDetail = findViewById(R.id.tv_itemDescription_itemDetail);

        //seller data display
        tvSellerFullNameItemDetail = findViewById(R.id.tv_sellerFullName_itemDetail);
        ivSellerPicItemDetail = findViewById(R.id.iv_sellerPic_itemDetail);
        tvSellerHasBeenFlaggedIndicator = findViewById(R.id.tv_seller_hasBeenFlaggedIndicator);
        ratingBarItemDetail = findViewById(R.id.ratingBar_itemDetail);//clickable
        tvAmountOfReviewsItemDetails = findViewById(R.id.tv_amountOfReviews_itemDetails);//clickable

        //clickables
        tvOtherUserItemsLinkItemDetail = findViewById(R.id.tv_otherUserItemsLink_itemDetail);
        btnSeeItemVideoOptItemDetail = findViewById(R.id.btn_seeItemVideo_opt_itemDetail);
        btnMakeOfferToSellerItemDetail = findViewById(R.id.btn_makeOfferToSeller_itemDetail);
        btnMsgSellerItemDetail = findViewById(R.id.btn_msgSeller_itemDetail);

        //if user sees his own item, hide buttons disallowing him to trade with himself
        if (item.getSeller_user_UUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            btnMakeOfferToSellerItemDetail.setVisibility(View.GONE);
            btnMsgSellerItemDetail.setVisibility(View.GONE);
        }

        //update ui for item data
        Picasso.get().load(item.getPictureURI()).into(ivImageItemDetail);
        tvItemTitleItemDetail.setText(item.getTitle());
        tvItemDescriptionItemDetail.setText(item.getDescription());
        Log.d("BOLO", "videoURI:" + item.getVideoURI());
        btnSeeItemVideoOptItemDetail.setVisibility(item.hasVideo() ? View.VISIBLE : View.GONE);//don't show button if there is no video


        //request seller data
        DB.users.child(item.getSeller_user_UUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDataModel seller = dataSnapshot.getValue(UserDataModel.class);
                if (seller != null) {
                    updateSellerDetails(seller);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BOLO", databaseError.getMessage());
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateSellerDetails(UserDataModel seller) {
        this.seller = seller;
        ratingBarItemDetail.setRating(seller.getAvgRev());
        tvAmountOfReviewsItemDetails.setText(String.valueOf(seller.getAmtRev()));
        tvSellerFullNameItemDetail.setText(seller.getFullName());
        tvSellerHasBeenFlaggedIndicator.setVisibility(seller.isFlagged() ? View.VISIBLE : View.GONE);
        Picasso.get().load(seller.getPicture()).into(ivSellerPicItemDetail);

    }

    public void GO_TO_WatchItemVideoActivity(View view) {
        // TODO: 17/11/2019  WatchItemVideoActivity
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("url", item.getVideoURI());
        startActivity(intent);
    }

    public void GO_TO_activity_ItemsBySeller(View view) {
        Intent intent = new Intent(this, ItemsListActivity.class);
        intent.putExtra("by", "uuid");
        intent.putExtra("uuid", item.getSeller_user_UUID());
        startActivity(intent);
    }

    public void makeOfferToSeller(View view) {
        // TODO: 17/11/2019 code
        Intent intent = new Intent(this, ItemsListActivity.class);
        ItemsListActivity.BY_ME(intent); // use static method enforcing proper intent protocol for ItemsList Activity
        startActivityForResult(intent, PICK_ITEM_FOR_OFFER);
    }

    public void GO_TO_activity_ChatSendMessageToSeller(View v) {
    GO_TO_activity_ChatSendMessageToSeller();
    }

    public void GO_TO_activity_ChatSendMessageToSeller() {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("otherUser",seller);
        startActivity(intent);
    }

    public void GO_TO_activity_SellerReviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("uuid", item.getSeller_user_UUID());
        startActivity(intent);
    }

    public void GO_TO_activity_SellerProfile(View view) {
        // TODO: 17/11/2019 code ProfileActivity
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra("uuid", item.getSeller_user_UUID());
//        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == PICK_ITEM_FOR_OFFER) {
            //do the things u wanted
            final ItemData pickedItem = (ItemData) data.getSerializableExtra(EXTRA_ITEM_KEY);

            Toast.makeText(this, "YOU ARE ABOUT TO OFFER YOUR ITEM: " + pickedItem.getTitle() + "FOR this item :" + item.getTitle(), Toast.LENGTH_SHORT).show();
            BarterTradeConfirmationDialog dialog = new BarterTradeConfirmationDialog(this, "Send Trade Offer ? " ,pickedItem, item);

            dialog.setOnOKClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizeOfferToSeller(pickedItem);
                }
            });

            dialog.show();
        }
    }

    private void finalizeOfferToSeller(final ItemData myItem) {
        //your prefix is for is currently viewed item and seller
        final ItemData yourItem = item;
        final String myID = DB.me.getUid();
        final String yourID = item.getSeller_user_UUID();


        final String messageThreadId = DB.calculateMessageThreadID(myID, yourID);
        Toast.makeText(this, messageThreadId, Toast.LENGTH_LONG).show();

        //new or refresh message thread
        MessageThreadDataModel msgThr = new MessageThreadDataModel(MessageDataModel.OFFER_BODY);// inbox of other user will say that the last message is "Offers Barter Trade"
        DB.messageThreads.child(messageThreadId).setValue(msgThr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   //update inboxes of me-user and you-seller
                                                   DB.users.child(yourID).child("inbox").child(messageThreadId).setValue(true); // booleans used as unread state
                                                   DB.users.child(myID).child("inbox").child(messageThreadId).setValue(false);// i send the message so it is not unread
                                                   String msgID = DB.messageThreads_messages.child(messageThreadId).push().getKey();//generate node key

                                                   MessageDataModel msgDM = MessageDataModel
                                                           .makeOfferMessage(yourID,myItem.getId(),yourItem.getId()); //use static method to make it easier looking

                                                   //finally add the offer-message to the thread
                                                   DB.messageThreads_messages.child(messageThreadId).child(msgID).setValue(msgDM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()){
                                                               Toast.makeText(ItemDetailsActivity.this, "Message Send successfully", Toast.LENGTH_SHORT).show();
                                                               //redirect user to enforce feedback - confirm sending
                                                               GO_TO_activity_ChatSendMessageToSeller();
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                       }
                );
    }
}

