package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageDataModelRVAdapter extends RecyclerView.Adapter<MessageDataModelRVAdapter.BaseMessageHolder>{
    private OnItemClickListener onItemClickListener;
    //    private Context mContext;
    private List<MessageDataModel> messageDataModels;
    //private Random rnd;
    private LayoutInflater mInflater;
    private static final int TYPE_OFFER_ME = 2;
    private static final int TYPE_MESSAGE_ME = 3;
    private static final int TYPE_OFFER_YOU = 4;
    private static final int TYPE_MESSAGE_YOU = 5;

    public interface OnItemClickListener {
        void onItemClick(MessageDataModel clickedMessageDataModel);
    }

    @Override
    public int getItemViewType(int position) {
        MessageDataModel messageDataModel  = messageDataModels.get(position);
        if (MessageDataModel.TYPE_MESSAGE.equalsIgnoreCase(messageDataModel.getType())){
            if (DB.currentUser.getUid().equals(messageDataModel.getFrom())){
                return TYPE_MESSAGE_ME;
            }
            else{
                return TYPE_MESSAGE_YOU;
            }
        }
        else if (MessageDataModel.TYPE_OFFER.equalsIgnoreCase(messageDataModel.getType())){
            if (DB.currentUser.getUid().equals(messageDataModel.getFrom())){
                return TYPE_OFFER_ME;
            }
            else{
                return TYPE_OFFER_YOU;
            }
        }
        else{
            return TYPE_MESSAGE_ME;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MessageDataModelRVAdapter(LayoutInflater mInflater, List<MessageDataModel> messageDataModels) {
        this.messageDataModels = messageDataModels;
        this.mInflater = mInflater;
    }

    public void updateItems(List<MessageDataModel> newList) {
        this.messageDataModels = newList;
        notifyDataSetChanged();
    }

    public void consume(MessageDataModel messageDataModel) {
        this.messageDataModels.add(messageDataModel);
        notifyDataSetChanged();

        //now request for User
    }

    @NonNull
    @Override
    public BaseMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRow;
        switch (viewType){
            case TYPE_MESSAGE_ME:
                viewRow = mInflater
                        .inflate(R.layout.recycler_view_my_message_item, parent, false);
                return new MessageHolder(viewRow);

            case TYPE_MESSAGE_YOU:
                viewRow = mInflater
                        .inflate(R.layout.recycler_view_your_message_item, parent, false);
                return new MessageHolder(viewRow);

            case TYPE_OFFER_ME:
                viewRow = mInflater
                        .inflate(R.layout.recycler_view_my_offer_item, parent, false);
                return new OfferHolder(viewRow);

            case TYPE_OFFER_YOU:
                viewRow = mInflater
                        .inflate(R.layout.recycler_view_your_offer_item, parent, false);
                return new OfferHolder(viewRow);

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BaseMessageHolder holder, int position) {
        MessageDataModel messageDataModel = messageDataModels.get(position);
        int viewType = getItemViewType(position);
        switch (viewType){
            case TYPE_MESSAGE_ME:
            case TYPE_MESSAGE_YOU:
                MessageHolder messageHolder = (MessageHolder) holder;
                bindMessageHolder(messageHolder,messageDataModel);
                break;

            case TYPE_OFFER_ME:
            case TYPE_OFFER_YOU:
                OfferHolder offerHolder = (OfferHolder) holder;
                bindOfferHolder(offerHolder,messageDataModel);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }



    }

    private void bindMessageHolder(MessageHolder holder, MessageDataModel messageDataModel) {
        String timestr = DB.calcDateStringFromTimestamp("kk:mm",messageDataModel.getTimestampLong());
        holder.tvTime.setText(timestr);
        holder.msgBody.setText(messageDataModel.getBody());
    }

    private void bindOfferHolder(OfferHolder holder, MessageDataModel messageDataModel) {
//holder.ivYourItemOfferMsg;
//holder.ivMyItemOfferMsg;
         String timestr = DB.calcDateStringFromTimestamp("kk:mm",messageDataModel.getTimestampLong());
holder.tvTime.setText(timestr);
holder.tvYourItemTitleOfferMsg.setText(messageDataModel.getOfferedItemId());
holder.tvMyItemTitleOfferMsg.setText(messageDataModel.getWantedItemId());
    }

    @Override
    public int getItemCount() {
        return messageDataModels.size();
    }

    // View holder

    /**
     * View holder
     */
    public class OfferHolder extends BaseMessageHolder{
        private CircleImageView ivYourItemOfferMsg;
        private CircleImageView ivMyItemOfferMsg;
        private TextView tvTime;
        private TextView tvYourItemTitleOfferMsg;
        private TextView tvMyItemTitleOfferMsg;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            ivYourItemOfferMsg = (CircleImageView) itemView.findViewById(R.id.iv_yourItem_OfferMsg);
            ivMyItemOfferMsg = (CircleImageView) itemView.findViewById(R.id.iv_myItem_OfferMsg);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvYourItemTitleOfferMsg = (TextView) itemView.findViewById(R.id.tv_yourItem_title_OfferMsg);
            tvMyItemTitleOfferMsg = (TextView) itemView.findViewById(R.id.tv_myItem_title_OfferMsg);
        }

    }

    public class MessageHolder extends BaseMessageHolder {
        private TextView tvTime;
        private TextView msgBody;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            msgBody = (TextView) itemView.findViewById(R.id.msg_body);
        }

    }

    public class BaseMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public BaseMessageHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(messageDataModels.get(position));
                }
            }
        }
    }

}

