package com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.Consumer;

public class OnFirebaseObjectLoaded<DataModelType> implements ValueEventListener {
        private Consumer<DataModelType> consumer;
        private int position;
        private Class<DataModelType> dataModelClass;

        public OnFirebaseObjectLoaded( Consumer<DataModelType> consumer, int position, Class<DataModelType> dataModelClass) {
            this.consumer = consumer;
            this.position = position;
            this.dataModelClass = dataModelClass;
        }

    public OnFirebaseObjectLoaded<DataModelType> copy(int newposition){
        return new OnFirebaseObjectLoaded<DataModelType>(consumer,newposition,dataModelClass);
    }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("BOLO","received snapshot position:" + position);
            if (!dataSnapshot.exists()){
                //this.onObjectNotFound(position);
                consumer.noObject(dataSnapshot.getKey(),position);

                // TODO: 26/11/2019 Consumer should have method on on objectnotfound
            }
            DataModelType dataModel = dataSnapshot.getValue(dataModelClass);// todo this could be just a field on Consumer<FFDataModel> - it would save a lot of hassle and offer other possibilities later (for example is user active and so on)
            consumer.consume(dataSnapshot.getKey(),dataModel, position);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("BOLO", databaseError.getMessage());
        }

}
