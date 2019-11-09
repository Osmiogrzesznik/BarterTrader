package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.Addable;

import java.util.List;

public class AddElementToListValueListener<T> implements ValueEventListener{

    private Context ctx;
    private final Addable<T> addable;
    private Class<T> tClass;

    public AddElementToListValueListener(Context ctx, Addable<T> addable, Class<T> tClass) {
        this.ctx = ctx;
        this.addable = addable;
        this.tClass = tClass;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        T item = dataSnapshot.getValue(tClass);
        addable.add(item);
        //here somthing should happen to inform addable that something has changed
        Log.d("BOLO", item.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
