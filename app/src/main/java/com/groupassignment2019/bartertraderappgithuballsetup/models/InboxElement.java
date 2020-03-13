package com.groupassignment2019.bartertraderappgithuballsetup.models;

import com.google.firebase.database.Exclude;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;

import java.io.Serializable;

public class InboxElement implements Serializable {
    //todo instead of all these weird fiels could just use OtherUser object as a member albeit dynamically loaded;

    private String lastMessageBody;
    private long lastMessageTime;
    private String WhoWroteLast;
    private String MessageThreadId;
    private boolean read;

    @Exclude
    private UserDataModel userInterlocutor;

    @Exclude
    public UserDataModel getUserInterlocutor() {
        return userInterlocutor;
    }
    @Exclude
    public void setUserInterlocutor(UserDataModel userInterlocutor) {
        this.userInterlocutor = userInterlocutor;
    }

//    @Exclude
//    private String UserInterlocutor_image;
//    @Exclude
//    private String UserInterlocutor_firstLastName;
//    @Exclude
    private String OtherUserID;

    @Exclude
    private boolean complete;// boolean used to identify if asynchronous tasks finished updating this object data

    @Exclude
    public boolean isComplete() {
        return complete;
    }

    @Exclude
    public void setComplete(boolean complete) {
        this.complete = complete;
    }


//    @Exclude
//    public String getUserInterlocutor_image() {
//        return UserInterlocutor_image;
//    }

    @Exclude
    public String getOtherUserID() {
        return OtherUserID;
    }

    @Exclude
    public void setOtherUserID(String otherUserID) {
        OtherUserID = otherUserID;
    }

//    @Exclude
//    public void setUserInterlocutor_image(String userInterlocutor_image) {
//        UserInterlocutor_image = userInterlocutor_image;
//    }

//    @Exclude
//    public String getUserInterlocutor_firstLastName() {
//        return UserInterlocutor_firstLastName;
//    }
//
//    @Exclude
//    public void setUserInterlocutor_firstLastName(String userInterlocutor_firstLastame) {
//        UserInterlocutor_firstLastName = userInterlocutor_firstLastame;
//    }

    public InboxElement() {
        this.complete = false;
    }

    public String getLastMessageBody() {
        return lastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }

    private long getLastMessageTime() {
        return lastMessageTime;
    }

    @Exclude
    public String getLastMessageTimeAsString() {

        return DB.calcDateStringFromTimestamp("dd-MM-yyyy kk:mm:ss",getLastMessageTime());
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
        String user;
        if(userInterlocutor == null){
            user = "NULL - Not loaded yet";
        }else{
            user = userInterlocutor.toString();
        }
        String output = "InboxElement{" +
                "lastMessageBody='" + lastMessageBody + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                ", WhoWroteLast='" + WhoWroteLast + '\'' +
                ", MessageThreadId='" + MessageThreadId + '\'' +
                ", read=" + read +
                ", OtherUserID='" + OtherUserID + '\'' +
                ", complete=" + complete +
                ", userInterlocutor=" + user +
                '}';
        return output;
    }
}
