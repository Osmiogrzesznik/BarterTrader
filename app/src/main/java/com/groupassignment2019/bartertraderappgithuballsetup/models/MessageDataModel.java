package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.CompleteOnlyWhenNeededObservable;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.OnFirebaseObjectLoaded;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.Consumer;

public class MessageDataModel implements Consumer<ItemData>, CompleteOnlyWhenNeededObservable {
    //Used in App, parts of interface
    // TODO: 26/11/2019 make all datamodels extend basedatamodel that contins key reference to itself setter/getterr excluded
    @Exclude
    public String key;

    @Exclude
    public final static String OFFER_BODY = "Offers Barter Trade";
    @Exclude
    private boolean complete = false;
    @Exclude
    private ItemData wantedItem;
    @Exclude
    private ItemData offeredItem;
    @Exclude
    private ObservingAdapter observer;
    @Exclude
    private int positionInAdapter;

    //Firebase Data
    private boolean isOffer;
    private String body;
    private String from;
    private String to;
    private long timestamp;
    private boolean agreed;// e.g. false,
    private String offeredItemId;// e.g. "0",
    private String wantedItemId;// e.g. "1"


    @Exclude
    public ItemData getWantedItem() {
        return wantedItem;
    }

    @Exclude
    public void setWantedItem(ItemData wantedItem) {
        this.wantedItem = wantedItem;
    }

    @Exclude
    public ItemData getOfferedItem() {
        return offeredItem;
    }

    @Exclude
    public void setOfferedItem(ItemData offeredItem) {
        this.offeredItem = offeredItem;
    }


    /**
     * not used by firebase since Timestamp is set on the server . This getter is only used on client side
     * @return timestamp as Long
     */
    @Exclude
    public long getTimestampLong() {
        return timestamp;
    }

    @Exclude
    public boolean isComplete(){
        if (isOffer) return wantedItem != null || offeredItem != null;
        else return true;
    }

    @Override
    public void consume(String key, ItemData itemData, int identifier) {
        if(identifier == 0){
            wantedItem = itemData;
        }else{
            offeredItem = itemData;
        }
        if(observer != null){
            observer.update(this,positionInAdapter);
        }
    }

    @Override
    public void noObject(String key, int identifier) {
        //one of items is nonexistent, delete yourself from DB, since transaction is impossible(item was agreed already to other transaction)
        if(observer != null){
            observer.update(null,positionInAdapter);
        }
    }

    public void completeYourselfAndNotifyObserver(ObservingAdapter o, int position){
        OnFirebaseObjectLoaded<ItemData> offeredItemLoad = new OnFirebaseObjectLoaded<>(this, 0, ItemData.class);
        OnFirebaseObjectLoaded<ItemData> wantedItemLoad = offeredItemLoad.copy(1);
        DB.items.child(getOfferedItemId()).addListenerForSingleValueEvent(offeredItemLoad);
        DB.items.child(getWantedItemId()).addListenerForSingleValueEvent(wantedItemLoad);
        this.observer = o;
        this.positionInAdapter = position;
    }

    // App-side pseudo-constructors, not used by Firebase, only during instantiation when user sends new message from app
    @Exclude
    public static MessageDataModel makeNormalMessage(String to,String body){
        MessageDataModel m = new MessageDataModel();
        m.isOffer = false;
        m.from = DB.me.getUid();
        m.body = body;
        m.to = to;
        return m;
    }

    @Exclude
    public static MessageDataModel makeOfferMessage(String to, String wantedItemId, String offeredItemId) {
        MessageDataModel m = new MessageDataModel();
        m.isOffer = true;
        m.from = DB.me.getUid();
        m.body = OFFER_BODY;
        m.to = to;
        m.agreed = false;
        m.offeredItemId = offeredItemId;
        m.wantedItemId = wantedItemId;
        return m;
    }

    //Firebase accessors and constructor;

    public MessageDataModel() {
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    private String getOfferedItemId() {
        return offeredItemId;
    }

    public void setOfferedItemId(String offeredItemId) {
        this.offeredItemId = offeredItemId;
    }

    private String getWantedItemId() {
        return wantedItemId;
    }

    public void setWantedItemId(String wantedItemId) {
        this.wantedItemId = wantedItemId;
    }


    //due to nature of firebase TIMESTAMP being map
    public java.util.Map<String, String> getTimestamp() {
        return ServerValue.TIMESTAMP;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
