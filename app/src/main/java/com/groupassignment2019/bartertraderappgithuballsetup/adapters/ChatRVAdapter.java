package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.CompleteOnlyWhenNeededObservable;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ObservingAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRVAdapter extends RecyclerView.Adapter<ChatRVAdapter.ClickableMessageHolder> implements ObservingAdapter {
    // TODO: 26/11/2019 it has to implement onChildEvent to display new messages in real time
    private OnItemClickListener onItemClickListener;
    private List<MessageDataModel> messages;
    private LayoutInflater mInflater;

    //By assigning ints for types consistent with layouts, i need only to check getItemViewType once and variable names make code self-explanatory;
    private static final int MY_OFFER = R.layout.recycler_view_my_offer_item;
    private static final int MY_MESSAGE = R.layout.recycler_view_my_message_item;
    public static final int YOUR_OFFER = R.layout.recycler_view_your_offer_item;
    private static final int YOUR_MESSAGE = R.layout.recycler_view_your_message_item;

    /**
     * @param mInflater dependency injected from activity
     * @param messages
     */
    public ChatRVAdapter(LayoutInflater mInflater, List<MessageDataModel> messages) {
        this.messages = messages;
        this.mInflater = mInflater;
    }

    /**
     * offer message is stored in db in such a way it doesn't know who is the viewing user
     * viewType is determined by comparing viewing user id with .from field of message and was calculated earlier
     * following this later we respectively assign values to xml views in such a way that
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        MessageDataModel message = messages.get(position);
        int output = -1;
        //TODO modify data model no need for FRom or To - since other user id is in key of message thread you can use only author field
        boolean authorIsMe = DB.isMe(message.getFrom());//compare Author userId with currently logged one;
        if (message.isOffer() && !message.isAgreed()) {
            output = authorIsMe ? MY_OFFER : YOUR_OFFER;
        } else {// normal message
            output = authorIsMe ? MY_MESSAGE : YOUR_MESSAGE;
        }

//        String debug = "";
//        switch(output){
//            case MY_MESSAGE:
//                debug = "MY_MESSAGE";
//                break;
//            case MY_OFFER:
//                debug = "MY_OFFER";
//                break;
//            case YOUR_MESSAGE:
//                debug = "YOUR_MESSAGE";
//                break;
//            case YOUR_OFFER:
//                debug = "YOUR_OFFER";
//                break;
//        }
//        Log.d("BOLO-viewtype",debug +message.isOffer());
        return output;
        //todo umiesc tutaj albo dodaj watcha wyswietlajacego nazwy takie jak my_offer itd
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * depending on type of message (offer or Normal message) different layouts are assigned
     * Offer holder requires displaying traded item's images
     *
     * @param parent
     * @param viewType_and_layoutID one of constants in this adapter : MY_OFFER,MY_MESSAGE,YOUR_OFFER,YOUR_MESSAGE
     * @return
     */

    @NonNull
    @Override
    public ClickableMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType_and_layoutID) {
        View viewRow;

        switch (viewType_and_layoutID) {

            case MY_MESSAGE:
            case YOUR_MESSAGE:
                viewRow = mInflater.inflate(viewType_and_layoutID, parent, false);
                return new MessageHolder(viewRow);

            case MY_OFFER:
            case YOUR_OFFER:
                viewRow = mInflater
                        .inflate(viewType_and_layoutID, parent, false);
                return new OfferHolder(viewRow);

            default:
                throw new IllegalStateException("Unexpected value: " + viewType_and_layoutID);
        }
    }


    /**
     * depending on type of message (offer or Normal message) different binding methods are called from here
     * Offer holder requires more complex operations since it displays traded item's images
     *
     * @param holder   generic holder type
     * @param position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ClickableMessageHolder holder, int position) {
        MessageDataModel message = messages.get(position);
        int viewType = holder.getItemViewType();
        //any type of message has a time
        String timestr = DB.calcDateStringFromTimestamp("kk:mm", message.getTimestampLong());
        holder.tvTime.setText(timestr);

        switch (viewType) {
            case MY_MESSAGE:
            case YOUR_MESSAGE:
                MessageHolder messageHolder = (MessageHolder) holder;
                bindMessageHolder(messageHolder, message);
                break;

            case MY_OFFER:
            case YOUR_OFFER:
                OfferHolder offerHolder = (OfferHolder) holder;
                bindOfferHolder(offerHolder, message, position, viewType);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }


    }

    /**
     * nothing special. Message holder contains only the message and time
     *
     * @param holder
     * @param message
     */
    private void bindMessageHolder(MessageHolder holder, MessageDataModel message) {
        holder.msgBody.setText(message.getBody());
    }

    /**
     * this binding is a little bit more complicated since the layout needs additional data for pictures and titles of items
     * first time when this recyclerview item slides into view, the item will be informed to request additional data from layout and
     * call adapter back when it is done by notifying through itemDatahasChanged(position) this will attempt to bind data again, this time
     * message being completed . This way images and data are downloaded only when needed ( since messages thread may go a long way into past it is wasteful to download
     * all data at once
     *
     * @param holder   OfferHolder
     * @param message  message that may be incomplete or complete (complete with foreign ItemData POJO )
     * @param position position of an item required to notify adapter about change in this particular dataItem in list
     * @param viewType to differentiate between who authored the offerMessage and place Item images correctly
     */
    private void bindOfferHolder(OfferHolder holder, MessageDataModel message, int position, int viewType) {
        if (message.isAgreed()){
            holder.ivMyItemImage.setVisibility(View.VISIBLE);
            holder.ivYourItemImage.setVisibility(View.GONE);// if trade was made there is no item to retrieve the images so make them gone
            holder.tvYourItemTitle.setVisibility(View.GONE);
            holder.tvMyItemTitle.setText(message.getBody());
            Picasso.get().load(R.drawable.ic_ok).into(holder.BgPic);
        }

        else if (message.isComplete()) { // if message contains the ItemData it refers to
            //defaults
            String offeredPicture = null, wantedPicture = null, wantedTitle = "Item was already traded", offeredTitle= wantedTitle;
            //In case when the item was traded in different offer for different user it will be null
            if (message.getOfferedItem() != null) {
                offeredPicture = message.getOfferedItem().getPictureURI();
                offeredTitle = message.getOfferedItem().getTitle();
            }
            if (message.getWantedItem() != null) {
                wantedPicture = message.getWantedItem().getPictureURI();
                wantedTitle = message.getWantedItem().getTitle();
            }
            switch (viewType) {
                // see getItemViewType for explanation
                // viewing user's item is in the top right corner - this follows any other messaging app (whatsapp, messenger)
                // convention - Viewer sees his own messages on the right side of the screen
                case MY_OFFER: //means viewing user is the user who wrote the message
                    Picasso.get().load(offeredPicture).into(holder.ivMyItemImage);
                    Picasso.get().load(wantedPicture).into(holder.ivYourItemImage);
                    holder.tvMyItemTitle.setText(offeredTitle);
                    holder.tvYourItemTitle.setText(wantedTitle);
                    break;
                case YOUR_OFFER: //means this offer came from the other user.
                    Picasso.get().load(offeredPicture).into(holder.ivYourItemImage);
                    Picasso.get().load(wantedPicture).into(holder.ivMyItemImage);
                    holder.tvYourItemTitle.setText(offeredTitle);
                    holder.tvMyItemTitle.setText(wantedTitle);
                    break;
                // TODO: 25/11/2019 it may now crash since if message was clicked agreed no item exists at snapshot
            }
        } else {
            //request for message to download complete data,adapter being an observer
            message.completeYourselfAndNotifyObserver(this, position);
        }
    }

    @Override
    public void update(CompleteOnlyWhenNeededObservable observable, int position) {
        if (observable == null) {
            //messages.remove(position);
        } else if (observable.isComplete()) {
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    // View holders

    /**
     * normal message holder
     */
    class MessageHolder extends ClickableMessageHolder {
        private TextView msgBody;

        MessageHolder(@NonNull View itemView) {
            super(itemView);
            msgBody = itemView.findViewById(R.id.msg_body);
        }

    }

    /**
     * View holder for offers
     */
    class OfferHolder extends ClickableMessageHolder {
        private CircleImageView ivYourItemImage;
        private CircleImageView ivMyItemImage;
        private TextView tvYourItemTitle;
        private TextView tvMyItemTitle;
private ImageView BgPic;
        OfferHolder(@NonNull View itemView) {
            super(itemView);
            BgPic = itemView.findViewById(R.id.iv_logoSwapArrows_OfferMsg);
            ivYourItemImage = itemView.findViewById(R.id.iv_yourItem_OfferMsg);
            ivMyItemImage = itemView.findViewById(R.id.iv_myItem_OfferMsg);
            tvYourItemTitle = itemView.findViewById(R.id.tv_yourItem_title_OfferMsg);
            tvMyItemTitle = itemView.findViewById(R.id.tv_myItem_title_OfferMsg);
        }
    }

    /**
     * Base Holder for above ones
     */
    public class ClickableMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView tvTime;

        ClickableMessageHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLongClickable(true);
            tvTime = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(messages.get(position), this.getItemViewType());
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            tvTime.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MessageDataModel clickedMessageDataModel, int itemViewType);
    }

}

