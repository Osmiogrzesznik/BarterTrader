package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;

public class MessageThread {
    private String lastMessageBody;
    private String lastMessageTime;
    private String WhoWroteLast;
    private boolean read;
    private long lastmessageTime;
    public String getLastMessageBody() {
        return lastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getWhoWroteLast() {
        return WhoWroteLast;
    }

    public void setWhoWroteLast(String whoWroteLast) {
        WhoWroteLast = whoWroteLast;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


}
