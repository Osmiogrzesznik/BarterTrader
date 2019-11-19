package com.groupassignment2019.bartertraderappgithuballsetup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

class UserDataLoader implements FirebaseAuth.AuthStateListener {
    private UserObserver userObserver;

    /**
     * Listener Loading logged in user data from Database and passing them to subscribing objects.
     * @param userObserver party interested in logged in user's data that comes from database
     */
    public UserDataLoader(UserObserver userObserver) {
        this.userObserver = userObserver;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        DB.getCurrentUserDataReference().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDataModel user = dataSnapshot.getValue(UserDataModel.class);
                        Log.d("BOLO", user.toString());
                        userObserver.updateUI(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}