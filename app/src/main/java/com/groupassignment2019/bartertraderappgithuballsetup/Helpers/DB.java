package com.groupassignment2019.bartertraderappgithuballsetup.Helpers;

import android.text.format.DateFormat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

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

    public static FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();

    public static DatabaseReference getMeReference(){
        return users.child(myUid());
    }

    /**
     * Generates/calculates firebase-fashion unique messagethreadID by appending lexicographically bigger uid to the smaller uid. It represents one_to_one unique relationship
     * and can be used to store unique Data object into firebase facilitating its retrieval by enforcing consistent protocol
     * @param uid1 user id
     * @param uid2 user id
     * @return always unique combination of two param id's
     */
    public static String calculateMessageThreadID(String uid1, String uid2) {
        // TODO again no need for second id , this is always calculated for currently logged in user of application - me
            // compare user ids lexicographically , we always want smaller uid at start
         if (uid1.compareTo(uid2) < 0) { // should be equivalent to uid1 < uid2
                return uid1 + uid2;
            } else {
                return uid2 + uid1;
            }
    }

    /**
     * "dd-MM-yyyy kk:mm:ss" format or any other provided combination, calculated from timestamp
     * @param s "dd-MM-yyyy kk:mm:ss"
     * @param timestamp
     * @return
     */
    public static String calcDateStringFromTimestamp(String s, long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format(s, cal).toString();
        return date;
    }

    public static boolean isMe(String userID) {
        return DB.myUid().equals(userID);
    }

    public static String myUid() {
        if (me != null) {
            return me.getUid();
        }
        else return null;
    }
}
