package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.groupassignment2019.bartertraderappgithuballsetup.ItemsListActivity.BOLO;

public class ListOfIdsValueEventListener implements ValueEventListener {

    private final Context ctx;
    private final ValueEventListener forEveryId;
    private DatabaseReference targetReferenceOfActualObjects;

    public ListOfIdsValueEventListener(Context ctx, DatabaseReference targetReferenceOfActualObjects, ValueEventListener forEveryId) {
        this.ctx = ctx;
        this.forEveryId = forEveryId;
        this.targetReferenceOfActualObjects = targetReferenceOfActualObjects;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot itemID : dataSnapshot.getChildren()) {
            String itemIDKey = itemID.getKey();
            Log.d(BOLO,itemIDKey);

            targetReferenceOfActualObjects.child(itemIDKey).addListenerForSingleValueEvent(forEveryId);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(BOLO, databaseError.getMessage());
        Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
    }
}
