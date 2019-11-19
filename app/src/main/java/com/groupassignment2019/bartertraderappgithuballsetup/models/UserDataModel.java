package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.Map;

public class UserDataModel {

    private String firstName;
    private String lastName;
    private String picture;
    private String phoneNumber;
    private String uid;
    private int avgRev;

    private int amtRev;
    private Map<String, Boolean> inbox;
    private String password;
    private Map<String, Boolean> tradedWith;
    private boolean flagged;

    public UserDataModel() {
    }

    public UserDataModel(String firstname, String lastname, String phone, String uid) {
        firstName = firstname;
        lastName = lastname;
        phoneNumber = phone;
        this.uid = uid;
    }


    @Override
    public String toString() {
        return "UserDataModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Exclude
    public String getFullName(){
     return firstName + " " + lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAvgRev() {
        return avgRev;
    }

    public void setAvgRev(int avgRev) {
        this.avgRev = avgRev;
    }

    public Map<String, Boolean> getInbox() {
        return inbox;
    }

    public void setInbox(Map<String, Boolean> inbox) {
        this.inbox = inbox;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Boolean> getTradedWith() {
        return tradedWith;
    }

    public void setTradedWith(Map<String, Boolean> tradedWith) {
        this.tradedWith = tradedWith;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isFlagged() {
        // TODO: 17/11/2019 cannot check if flagged boolean equals null? how to check it?
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getAmtRev() {
        return amtRev;
    }

    public void setAmtRev(int amtRev) {
        this.amtRev = amtRev;
    }


    @Exclude
    @NonNull
    public int calculateUnreadThreadAmount() {
        int unreadThreadsAmount = 0; // if no inboxes or no new messages it always be non Null value
        Collection<Boolean> inbox = getInbox().values();
        if (inbox != null) {
            for (boolean b : inbox) {
                if (b) unreadThreadsAmount++;
            }
        }
        return unreadThreadsAmount;
    }


}
