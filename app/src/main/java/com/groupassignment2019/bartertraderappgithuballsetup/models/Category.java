package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;


import com.groupassignment2019.bartertraderappgithuballsetup.R;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public static String[] CATEGORIES = {
            "Clothes",
            "Gadgets",
            "Tools",
            "Toys",
//            "bicycles",
    };

    public static String[] DESCRIPTIONS = {
            "Clothes, shoes, sportwear",
            "Mobile devices, consoles, electronics",
            "DIY, materials, equipment",
            "For Kids and For Grownups",
//            "bicycles",
    };

    public static int[] BACKGROUND_DRAWABLES = {
            R.drawable.back_bright,
            R.drawable.back_medium,
            R.drawable.back_dark,
            R.drawable.back_darkest,
//            R.drawable.ic_cat_bicycles,
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

            ALL_CATEGORIES.add(new Category(CATEGORIES[i], ICONS[i], BACKGROUND_DRAWABLES[i], DESCRIPTIONS[i]));

        }

        return ALL_CATEGORIES;
    }


    public String categoryTitle;
    public Uri imageUri;


    public int backgroundDrawableId;
    public int imageResource;
    public String description;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }


    public Category(String categoryTitle, int imageResource,int backgroundDrawableId, String description) {
        this.categoryTitle = categoryTitle;
        this.backgroundDrawableId = backgroundDrawableId;
        this.imageResource = imageResource;
        this.description = description;
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
