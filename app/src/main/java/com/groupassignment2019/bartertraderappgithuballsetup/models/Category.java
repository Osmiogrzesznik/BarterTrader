package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;


import com.groupassignment2019.bartertraderappgithuballsetup.R;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public static String[] CATEGORIES = {
            "clothes",
//            "CD DVD",
//            "sport",
            "gadgets",
            "tools",
            "toys",
//            "phones",
//            "watches",
            "bicycles",
//            "cameras",
//            "for pets",
//            "newborn",
//            "cars",
//            "no money service",
//            "nomoney",
//            "service"
    };

    public static int[] ICONS = {
            R.drawable.ic_cat_clothes,
//            R.drawable.ic_cat_cd_dvd,
//            R.drawable.ic_cat_sports2,
            R.drawable.ic_cat_gadgets,
            R.drawable.ic_cat_tools,
            R.drawable.ic_cat_toys,
//            R.drawable.ic_cat_phones,
//            R.drawable.ic_cat_watches,
            R.drawable.ic_cat_bicycles,
//            R.drawable.ic_cat_cameras,
//            R.drawable.ic_cat_for_pets,
//            R.drawable.ic_cat_newborn,
//            R.drawable.ic_cat_cars,
//            R.drawable.ic_btapp_logo,
//            R.drawable.ic_cat_nomoneyservice2,
//            R.drawable.ic_cat_nomoneyservice
    };

    private static ArrayList<Category> ALL_CATEGORIES;

    public static List<Category> getAllCategories(){

        if(ALL_CATEGORIES != null){
            return ALL_CATEGORIES;
        }
        ALL_CATEGORIES = new ArrayList<Category>();

        for (int i = 0; i < CATEGORIES.length ; i++) {

            ALL_CATEGORIES.add(new Category(CATEGORIES[i], ICONS[i]));

        }

        return ALL_CATEGORIES;
    }


    String categoryTitle;
    Uri imageUri;



    int imageResource;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Category(String categoryTitle, int imageResource) {
        this.categoryTitle = categoryTitle;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
