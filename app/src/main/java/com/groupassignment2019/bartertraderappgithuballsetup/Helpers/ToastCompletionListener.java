package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ToastCompletionListener implements DatabaseReference.CompletionListener, OnCompleteListener<Void> {
    private Context context;
    private String partofmessage;

    public ToastCompletionListener(Context context, String partofmessage) {
        this.context = context;
        this.partofmessage = partofmessage;
    }



    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
        String msgSuccOrNot = databaseError == null ? " succeeded " : " failed due to " + databaseError.getMessage();
        produceMessage(msgSuccOrNot);
    }

    private void produceMessage(String msg){
        Toast.makeText(context, partofmessage + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        String msgSuccOrnot = task.isSuccessful() ?  " succeeded " : " failed due to " + task.getException().getMessage();
        produceMessage(msgSuccOrnot);
    }
}
