package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ItemData implements Serializable {

    @Exclude
    public static final ItemData EMPTY_TEST() {
        return EMPTY_TEST("testTitle");
    }

    @Exclude
    public static final ItemData EMPTY_TEST(String title){
    return new ItemData("99999999", title, "testDesc", "http://lorempixel.com/160/90","testCategory", "testUUID");
    }
    private String title;
    private String description;
    private String pictureURI;
    private String category;
    private String seller_user_UUID;
    private String videoURI;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemData() {
        // empty constructor for Firebase retrieval
    }

    public ItemData(String id,String title, String description, String pictureURI, String category, String seller_user_UUID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pictureURI = pictureURI;
        this.category = category;
        this.seller_user_UUID = seller_user_UUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURI() {
        return pictureURI;
    }

    public void setPictureURI(String pictureURI) {
        this.pictureURI = pictureURI;
    }

    public String getSeller_user_UUID() {
        return seller_user_UUID;
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pictureURI=" + pictureURI +
                ", seller_user_UUID='" + seller_user_UUID + '\'' +
                '}';
    }

    public void setSeller_user_UUID(String seller_user_UUID) {
        this.seller_user_UUID = seller_user_UUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVideoURI(String videoURI) {
        this.videoURI = videoURI;
    }
    public String getVideoURI() {
        return videoURI;
    }

    @Exclude
    public boolean hasVideo() {
        if (videoURI == null || videoURI == ""){
            return false;
        }
        else{
            return true;
        }
    }

}

