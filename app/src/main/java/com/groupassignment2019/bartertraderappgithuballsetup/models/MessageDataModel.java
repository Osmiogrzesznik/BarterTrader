package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;

public class MessageDataModel {
    @Exclude
    public final static String TYPE_MESSAGE = "message";
    @Exclude
    public final static String TYPE_OFFER = "offer";
    @Exclude
    public final static String OFFER_BODY = "Offers Barter Trade";

    private String body;
    private String from;
    private String to;
    private String type;
    private long timestamp;
    private boolean agreed;// e.g. false,
    private String offeredItemId;// e.g. "0",
    private String wantedItemId;// e.g. "1"

    public MessageDataModel() {
    }

    public MessageDataModel(String to, String body) {
        this.type = TYPE_MESSAGE;
        this.from = DB.currentUser.getUid();
        this.body = body;
        this.to = to;
    }

    public MessageDataModel(String to, String offeredItemId, String wantedItemId) {
        this.type = TYPE_OFFER;
        this.from = DB.currentUser.getUid();
        this.body = OFFER_BODY;
        this.to = to;
        this.agreed = false;
        this.offeredItemId = offeredItemId;
        this.wantedItemId = wantedItemId;
    }

    @Exclude
    public long getTimestampLong() {
        return timestamp;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public String getOfferedItemId() {
        return offeredItemId;
    }

    public void setOfferedItemId(String offeredItemId) {
        this.offeredItemId = offeredItemId;
    }

    public String getWantedItemId() {
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
