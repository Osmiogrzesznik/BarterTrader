package com.groupassignment2019.bartertraderappgithuballsetup.models;


import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDataModel implements Serializable {

    private String firstName;
    private String lastName;
    private String picture;
    private String phoneNumber;
    private String uid;
    private Integer avgRev;

    private Integer amtRev;
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
        amtRev = 0;
        avgRev = 0;
        flagged = false;

        this.uid = uid;
    }


    @Override
    public String toString() {
        return "UserDataModel{" +
                "fullName:" + getFullName() + " ," +
                "uid:" + uid + " " +
                '}';
    }

    @Exclude
    public String getFullName(){
     return firstName + " " + lastName;
    }

    public String getUuid() {
        return uid;
    }

    public void setUuid(String uid) {
        this.uid = uid;
    }

    public Integer getAvgRev() {
        if ( avgRev == null){
            return 0;
        }
        return avgRev;
    }

    public void setAvgRev(Integer avgRev) {
        this.avgRev = avgRev == null? 0:avgRev;
    }

    public Map<String, Boolean> getInbox() {
        return inbox == null ? new HashMap<String, Boolean>():inbox;
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
        return tradedWith == null ? new HashMap<String, Boolean>():tradedWith;
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

    public Integer getAmtRev() {
        return amtRev == null ? 0:amtRev;
    }

    public void setAmtRev(Integer amtRev) {
        this.amtRev = amtRev;
    }


    @Exclude
    @NonNull
    public Integer calculateUnreadThreadAmount() {
        Integer unreadThreadsAmount = 0; // if no inboxes or no new messages it always be non Null value
        if (getInbox() == null){
            return 0;
        }
        Collection<Boolean> inbox = getInbox().values();
        if (inbox != null) {
            for (boolean b : inbox) {
                if (b) unreadThreadsAmount++;
            }
        }
        return unreadThreadsAmount;
    }


}
