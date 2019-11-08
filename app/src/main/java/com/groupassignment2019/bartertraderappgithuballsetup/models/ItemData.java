package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.net.URI;

public class ItemData {
    @Exclude
    public static final ItemData EMPTY_TEST() {
        return EMPTY_TEST("testTitle");
    }

    @Exclude
    public static final ItemData EMPTY_TEST(String title){
    return new ItemData(title, "testDesc", Uri.parse("http://lorempixel.com/160/90"),"testCategory", "testUUID");
    }
    private String title;
    private String description;
    private Uri pictureURI;
    private String category;
    private String seller_user_UUID;

    public ItemData() {
    }

    public ItemData(String title, String description, Uri pictureURI, String category, String seller_user_UUID) {
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

    public Uri getPictureURI() {
        return pictureURI;
    }

    public void setPictureURI(String pictureURI) {
        this.pictureURI = Uri.parse(pictureURI);
    }

    public String getSeller_user_UUID() {
        return seller_user_UUID;
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pictureURI=" + pictureURI.toString() +
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
}

