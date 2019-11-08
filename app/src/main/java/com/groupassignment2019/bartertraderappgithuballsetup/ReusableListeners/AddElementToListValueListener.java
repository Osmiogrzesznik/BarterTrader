package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.ItemsByCategoryActivity;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;

import java.util.List;

public class AddElementToListValueListener<T> implements ValueEventListener {

    private Context ctx;
    private final List<T> tList;
    private final RecyclerView.Adapter adapter;
    private Class<T> tClass;

    public AddElementToListValueListener(Context ctx, List<T>tList, RecyclerView.Adapter adapter, Class<T> tClass) {
        this.ctx = ctx;
        this.tList = tList;
        this.adapter = adapter;
        this.tClass = tClass;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        T item = dataSnapshot.getValue(tClass);
        tList.add(item);
        adapter.notifyDataSetChanged();
        //here somthing should happen to inform adapter that something has changed
        Log.d("BOLO", item.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
