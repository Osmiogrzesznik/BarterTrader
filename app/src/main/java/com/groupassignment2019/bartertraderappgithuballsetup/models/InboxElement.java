package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.text.format.DateFormat;

import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.Locale;

public class InboxElement {
    //todo instead of all these weird fiels could just use OtherUser object as a member albeit dynamically loaded;
    private String lastMessageBody;
    private long lastMessageTime;
    private String WhoWroteLast;
    private String MessageThreadId;
    private boolean read;

    @Exclude
    private String UserInterlocutor_image;
    @Exclude
    private String UserInterlocutor_firstLastName;
    @Exclude
    private String OtherUserID;
    @Exclude
    private boolean complete;

    @Exclude
    public boolean isComplete() {
        return complete;
    }

    @Exclude
    public void setComplete(boolean complete) {
        this.complete = complete;
    }


    @Exclude
    public String getUserInterlocutor_image() {
        return UserInterlocutor_image;
    }

    @Exclude
    public String getOtherUserID() {
        return OtherUserID;
    }

    @Exclude
    public void setOtherUserID(String otherUserID) {
        OtherUserID = otherUserID;
    }

    @Exclude
    public void setUserInterlocutor_image(String userInterlocutor_image) {
        UserInterlocutor_image = userInterlocutor_image;
    }

    @Exclude
    public String getUserInterlocutor_firstLastName() {
        return UserInterlocutor_firstLastName;
    }

    @Exclude
    public void setUserInterlocutor_firstLastName(String userInterlocutor_firstLastame) {
        UserInterlocutor_firstLastName = userInterlocutor_firstLastame;
    }

    public InboxElement() {
        this.complete = false;
    }

    public String getLastMessageBody() {
        return lastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    @Exclude
    public String getLastMessageTimeAsString() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(getLastMessageTime() * 1000);
        String date = DateFormat.format("dd-MM-yyyy kk:mm:ss", cal).toString();
        return date;
    }


    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getWhoWroteLast() {
        return WhoWroteLast;
    }

    public void setWhoWroteLast(String whoWroteLast) {
        WhoWroteLast = whoWroteLast;
    }

    public String getMessageThreadId() {
        return MessageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        MessageThreadId = messageThreadId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "InboxElement{" +
                "lastMessageBody='" + lastMessageBody + '\'' +
                ", lastMessageTime='" + lastMessageTime + '\'' +
                ", WhoWroteLast='" + WhoWroteLast + '\'' +
                ", MessageThreadId='" + MessageThreadId + '\'' +
                ", read=" + read +
                ", UserInterlocutor_image=" + UserInterlocutor_image +
                ", UserInterlocutor_firstLastName='" + UserInterlocutor_firstLastName + '\'' +
                ", OtherUserID='" + OtherUserID + '\'' +
                ", complete=" + complete +
                '}';
    }
}
