package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

// TODO: 16/11/2019 REDUNDANT !!! this is just another ListOfIdsListener
public class StringListUpdater implements ValueEventListener {
private List<String> list;
private TextView tv;
private Context ct;

    public StringListUpdater(Context t ,List<String> list, TextView tvp) {
        this.tv = tvp;
        this.ct = t;
        this.list = list;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        list.clear();
        Toast.makeText(ct, "got data ", Toast.LENGTH_SHORT).show();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            list.add(ds.getKey());
        }
        update();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
tv.setText(databaseError.getMessage());
    }

    public void update(){
        Toast.makeText(ct, "", Toast.LENGTH_SHORT).show();
        tv.setText(list.toString());
    }


}
