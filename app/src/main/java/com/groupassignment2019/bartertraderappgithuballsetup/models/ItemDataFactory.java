package com.groupassignment2019.bartertraderappgithuballsetup.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ItemDataFactory {

    public List<ItemData> listOfItemDatas(){
        ArrayList<ItemData> l= new ArrayList<>();
        for(int i=0;i<=10;i++){
            ItemData id = new ItemData("title"+i,"description"+i, Uri.parse("http://lorempixel.com/400/200/"), "usudisid123");

            l.add(id);
        }
            return  l;
    }
}
