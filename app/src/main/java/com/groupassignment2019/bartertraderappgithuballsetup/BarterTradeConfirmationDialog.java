package com.groupassignment2019.bartertraderappgithuballsetup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BarterTradeConfirmationDialog extends Dialog{
    private final String question;
    private ItemData myItem;
    private ItemData yourItem;
    private ImageView ivPopupOKBtn;
    private CircleImageView ivYourItemImage;
    private CircleImageView ivMyItemImage;
    private TextView tvYourItemTitle;
    private TextView tvMyItemTitle;
    private ImageView ivPopupCANCELBtn;
    private View.OnClickListener OnOkClickListener;
    private TextView tvPopupQuestion;
    private int layoutResource = R.layout.popup_confirm_offer;
    private boolean isJustFeedback;

    public BarterTradeConfirmationDialog(@NonNull Context context, String question , ItemData myItem, ItemData yourItem) {
        super(context);
        this.myItem = myItem;
        this.yourItem = yourItem;
        this.question = question;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(this.layoutResource);
        super.onCreate(savedInstanceState);

        //find views
        tvPopupQuestion = findViewById(R.id.customTitlePopup);
        tvMyItemTitle = findViewById(R.id.tv_MyItem_title_popup);
        tvYourItemTitle = findViewById(R.id.tv_YourItem_title_popup);
        ivMyItemImage = findViewById(R.id.iv_MyItem_popup);
        ivYourItemImage = findViewById(R.id.iv_YourItem_popup);
        ivPopupOKBtn = findViewById(R.id.iv_popup_OK_btn);
        ivPopupCANCELBtn = findViewById(R.id.iv_popup_CANCEL_btn);


        // set images
        Picasso.get().load(myItem.getPictureURI()).into(ivMyItemImage);
        Picasso.get().load(yourItem.getPictureURI()).into(ivYourItemImage);

        // set the components to display the data
        tvPopupQuestion.setText(question);
        tvMyItemTitle.setText(myItem.getTitle());
        tvYourItemTitle.setText(yourItem.getTitle());


        // set the components to display the data
        tvPopupQuestion.setText(question);
        tvMyItemTitle.setText(myItem.getTitle());
        tvYourItemTitle.setText(yourItem.getTitle());

        // set click listeners for buttons
        ivPopupCANCELBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ivPopupOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnOkClickListener != null){
                    OnOkClickListener.onClick(v);
                }
                dismiss();
            }
        });

    }

    public void setOnOKClickListener(View.OnClickListener clickListener){
        OnOkClickListener = clickListener;
    }

    public void isTradeFinalized(boolean b) {
        this.isJustFeedback = b;
        layoutResource = isJustFeedback ? R.layout.popup_confirm_trade_finalized_successfully :R.layout.popup_confirm_offer;
    }
}
