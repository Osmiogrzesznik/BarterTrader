package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;


import com.groupassignment2019.bartertraderappgithuballsetup.R;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public static String[] CATEGORIES = {
            "clothes",
            "gadgets",
            "tools",
            "toys",
//            "bicycles",
    };

    public static int[] ICONS = {
            R.drawable.ic_cat_clothes,
            R.drawable.ic_cat_gadgets,
            R.drawable.ic_cat_tools,
            R.drawable.ic_cat_toys,
//            R.drawable.ic_cat_bicycles,
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
