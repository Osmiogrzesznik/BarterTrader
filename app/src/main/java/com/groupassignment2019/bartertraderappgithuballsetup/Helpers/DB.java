package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.Objects;

public class DB {
    public static FirebaseAuth Auth = FirebaseAuth.getInstance();
    static DatabaseReference rootpriv = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference root = rootpriv;
    public static DatabaseReference users = root.child("users");
    public static DatabaseReference categories = root.child("categories");
    public static DatabaseReference messageThreads = root.child("messageThreads");
    public static DatabaseReference messageThreads_messages = root.child("messageThreads_messages");
    public static DatabaseReference items_by_user = root.child("items_by_user");
    public static DatabaseReference items = root.child("items");
    public static DatabaseReference user_reviews = root.child("user_reviews");

    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public static DatabaseReference getCurrentUserDataReference(){
        return users.child(currentUser.getUid());
    }
}
