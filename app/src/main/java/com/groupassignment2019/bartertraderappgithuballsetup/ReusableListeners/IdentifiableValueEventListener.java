package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.Consumer;

public class IdentifiableValueEventListener<T> implements ValueEventListener{

    private Context ctx;
    private final int identifier;
    private final Consumer<T> consumer;
    private Class<T> tClass;
    private OnBeforeAddedListener<T> onBeforeAddedListener;

    public IdentifiableValueEventListener(Context ctx, Consumer<T> consumer, Class<T> tClass, int identifier) {
        this.ctx = ctx;
        this.identifier = identifier;
        this.consumer = consumer;
        this.tClass = tClass;
    }

    public IdentifiableValueEventListener copy(int identifier){
        return new IdentifiableValueEventListener(ctx,consumer,tClass,identifier);
    }

    public void setOnBeforeAddedListener(OnBeforeAddedListener<T> onBeforeAddedListener){
        this.onBeforeAddedListener = onBeforeAddedListener;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        T item = dataSnapshot.getValue(tClass);
        if (this.onBeforeAddedListener != null){
            item = onBeforeAddedListener.OnBeforeAdded(item,identifier); // transform item before adding to adapter if user specified
        }
        consumer.consume(dataSnapshot.getKey(),item, identifier);
        //here somthing should happen to inform consumer that something has changed
        Log.d("BOLO", item.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Transforms Added Item before adding to consumer
     * @param <T>
     */
    public interface OnBeforeAddedListener<T> {
        public T OnBeforeAdded(T element,int identifier);
    }
}
