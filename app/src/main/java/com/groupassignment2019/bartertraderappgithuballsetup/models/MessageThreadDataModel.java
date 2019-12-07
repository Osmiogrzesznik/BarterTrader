package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;

public class MessageThreadDataModel {

    //if client instantiates Message Thread do not initialize timestamp, it will e done on the server
    //this is only used on client side and alawys initialized by writing new message so getUID inside here

    public MessageThreadDataModel(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
        this.whoWroteLast = DB.myUid();
        this.read = false;
    }

    private String lastMessageBody ;
private long lastMessageTime ;
private boolean read ;
private String whoWroteLast ;

    public String getLastMessageBody() {
        return lastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }

    public java.util.Map<String,String> getLastMessageTime() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public long getLastMessageTimeLong() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getWhoWroteLast() {
        return whoWroteLast;
    }

    public void setWhoWroteLast(String whoWroteLast) {
        this.whoWroteLast = whoWroteLast;
    }

    public MessageThreadDataModel() {
    }
}
