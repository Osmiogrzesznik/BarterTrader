package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.StringListUpdater;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
// TODO: 11/11/2019 registerView xml is broken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textView);
        ArrayList<String> as = new ArrayList<>();
        as.add("kupa");
        StringListUpdater stringListUpdater = new StringListUpdater(this, as, tv);
        stringListUpdater.update();
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference ref = fb.getReference();
        ref.child("categories").addListenerForSingleValueEvent(stringListUpdater);
        View v = new View(this);

        //GO_TO_activity_Register(new View(this));
        //when you create your activity uncomment the appropriate code that redirects to it,
        // if your activity has different name, rename it by pressing CTRL + F6 on the file name(recommended)
        // , or rename class inside functions below (not recommended for clarity)
    }

    public void GO_TO_activity_Welcome(View view) {
//        Intent intent = new Intent(this, WelcomeActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_Register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_Dashboard(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_MyProfile(View view) {
//        Intent intent = new Intent(this, MyProfileActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_PostNewItem(View view) {
        Intent intent = new Intent(this, PostNewItemActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_Categories(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void GO_TO_activity_ItemDetails(View view) {
//        Intent intent = new Intent(this, ItemDetailsActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_SellerProfile(View view) {
//        Intent intent = new Intent(this, SellerProfileActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Chat(View view) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Inbox(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_SellerReviews(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
                    intent.putExtra("uuid", "0uuub5A248c530044dbCB24ADDa7");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_MyReviews(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
                    intent.putExtra("uuid", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void GO_TO_activity_ItemsByMe(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    intent.putExtra("by", "uuid");
                    intent.putExtra("uuid", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void GO_TO_activity_ItemsByCategory(View view) {
        Intent intent = new Intent(this, ItemsListActivity.class);
        intent.putExtra("by", "category");
        intent.putExtra("category", "gadgets");
        startActivity(intent);
    }

    public void GO_TO_activity_ItemsBySeller(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    intent.putExtra("by", "uuid");
                    intent.putExtra("uuid", "0uuub5A248c530044dbCB24ADDa7"); //onni kivela
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GOT_TO_WatchItemVideoActivity(View view) {
//        Intent intent = new Intent(this, WatchItemVideoActivity.class);
//        startActivity(intent);
    }
}
