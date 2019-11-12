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

public class AddElementToAdapterValueListener<T> implements ValueEventListener{

    private Context ctx;
    private final Addable<T> addable;
    private Class<T> tClass;
    private OnBeforeAddedListener<T> onBeforeAddedListener;

    public AddElementToAdapterValueListener(Context ctx, Addable<T> addable, Class<T> tClass) {
        this.ctx = ctx;
        this.addable = addable;
        this.tClass = tClass;
    }

    public AddElementToAdapterValueListener copy(){
        return new AddElementToAdapterValueListener(ctx,addable,tClass);
    }

    public void setOnBeforeAddedListener(OnBeforeAddedListener<T> onBeforeAddedListener){
        this.onBeforeAddedListener = onBeforeAddedListener;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        T item = dataSnapshot.getValue(tClass);
        if (this.onBeforeAddedListener != null){
            item = onBeforeAddedListener.OnBeforeAdded(item); // transform item before adding to adapter if user specified
        }
        addable.add(item);
        //here somthing should happen to inform addable that something has changed
        Log.d("BOLO", item.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Transforms Added Item before adding to addable
     * @param <T>
     */
    public interface OnBeforeAddedListener<T> {
        public T OnBeforeAdded(T element);
    }
}
