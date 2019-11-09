package com.groupassignment2019.bartertraderappgithuballsetup.models;

public class ReviewDataModel {

    private String authorUID;
    private String receiverUID;//? Redundant
    private String title;
    private String body;
    private int rating;

    public ReviewDataModel() {
    }


    public ReviewDataModel(String authorUID, String receiverUID, String title, String body, int rating) {
        this.authorUID = authorUID;
        this.receiverUID = receiverUID;
        this.title = title;
        this.body = body;
        this.rating = rating;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewDataModel{" +
                "authorUID='" + authorUID + '\'' +
                ", receiverUID='" + receiverUID + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }


}
