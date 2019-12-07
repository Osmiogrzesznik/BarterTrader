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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.StringListUpdater;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private static final int PICK_ITEM_FOR_OFFER = 2;
    private Random rnd = new Random();
    private TextView tv;
    private TextView userdetails;
    private FirebaseUser loggedinuser;


    // TODO: 11/11/2019 registerView xml is broken
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth.getInstance().addAuthStateListener(this);
        //FirebaseAuth.getInstance().signOut();
        userdetails = (TextView) findViewById(R.id.userdetails);


        tv = findViewById(R.id.textView);
        ArrayList<String> as = new ArrayList<>();
        as.add("kupa");
        StringListUpdater stringListUpdater = new StringListUpdater(this, as, tv);
        stringListUpdater.update();
        DB.categories.addListenerForSingleValueEvent(stringListUpdater);
        View v = new View(this);
//
        // GO_TO_activity_SellerReviews(v);
//        GO_TO_activity_PostNewItem(v);
//        GO_TO_activity_ItemDetails(v); // this crashes ?
        //GO_TO_activity_Inbox(v);
//        GO_TO_activity_ItemsByCategory(v);
        // GO_TO_activity_Dashboard(v);
//        GO_TO_activity_Register(v);
        //when you create your activity uncomment the appropriate code that redirects to it,
        // if your activity has different name, rename it by pressing CTRL + F6 on the file name(recommended)
        // , or rename class inside functions below (not recommended for clarity)
    }

    public void GO_TO_activity_Welcome(View view) {
//        Intent intent = new Intent(this, WelcomeActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_Login(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void GO_TO_activity_Register(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void GO_TO_activity_Dashboard(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            return;
        }

        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("janne.bay@example.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
//                    Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
//                    intent.putExtra("uuid", "0uuub5A248c530044dbCB24ADDa7");
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
//                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_MyProfile(View view) {
//        Intent intent = new Intent(this, MyProfileActivity.class);
//        startActivity(intent);
    }

    public void GO_TO_activity_PostNewItem(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, PostNewItemActivity.class);
            startActivity(intent);
            return;
        }

        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, PostNewItemActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_Categories(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(intent);
            return;
        }

        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_ItemDetails(View view) {
        final int itemId = rnd.nextInt(337);
        if (true) {//DB.me == null){ //TODO this is really worrisome
            final FirebaseAuth fa = FirebaseAuth.getInstance();

            if (loggedinuser != null) {
                DB.items.child(String.valueOf(itemId)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ItemData itemData = dataSnapshot.getValue(ItemData.class);
                        Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                        intent.putExtra("item", itemData);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        tv.setText("failed db connection ?");
                    }
                });
                return;
            }


            fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user = fa.getCurrentUser();
                    if (task.isSuccessful() && user != null) {
                        DB.items.child(String.valueOf(itemId)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                                intent.putExtra("item", itemData);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                tv.setText("failed db connection ?");
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


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
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, InboxActivity.class);
            startActivity(intent);
            return;
        }
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
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
            intent.putExtra("uuid", "0uuub5A248c530044dbCB24ADDa7");
            startActivity(intent);
            return;
        }
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
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
            intent.putExtra("uuid", loggedinuser.getUid());
            startActivity(intent);
            return;
        }
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
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
            ItemsListActivity.BY_ME(intent);
            startActivity(intent);
            return;
        }
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    ItemsListActivity.BY_ME(intent);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_ItemsByCategory(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(this, ItemsListActivity.class);
            ItemsListActivity.BY_CATEGORY(intent, "gadgets");
            startActivity(intent);
            return;
        }
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    ItemsListActivity.BY_CATEGORY(intent, "gadgets");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_ItemsBySeller(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
            ItemsListActivity.BY_USER(intent, "0uuub5A248c530044dbCB24ADDa7"); //onni kivela
            startActivity(intent);
            return;
        }
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    ItemsListActivity.BY_USER(intent, "0uuub5A248c530044dbCB24ADDa7"); //onni kivela
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_activity_MakeOffer(View view) {
        if (loggedinuser != null) {
            Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
            ItemsListActivity.BY_ME(intent);
            startActivityForResult(intent, PICK_ITEM_FOR_OFFER);
            return;
        }
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    Intent intent = new Intent(MainActivity.this, ItemsListActivity.class);
                    ItemsListActivity.BY_ME(intent);
                    startActivityForResult(intent, PICK_ITEM_FOR_OFFER);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GO_TO_WatchItemVideoActivity(View view) {
//        Intent intent = new Intent(this, WatchItemVideoActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == PICK_ITEM_FOR_OFFER) {
            //do the things u wanted
            ItemData item = (ItemData) data.getSerializableExtra("item");
            tv.setText(item.getTitle());
        }
    }

    public void GO_TO_WriteNewReviewActivity(View view) {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        if (loggedinuser != null) {
            GO_TO_activity(WriteNewReviewActivity.class);
            return;
        }
        fa.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fa.getCurrentUser();
                if (task.isSuccessful() && user != null) {
                    GO_TO_activity(WriteNewReviewActivity.class);
                } else {
                    Toast.makeText(MainActivity.this, "unsuccesful login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void GO_TO_activity(Class activityclass) {
        Intent intent = new Intent(MainActivity.this, activityclass);
        startActivity(intent);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            return;
        }
        loggedinuser = firebaseAuth.getCurrentUser();
        String userdetailstxt = firebaseAuth.getCurrentUser().getUid() + "\n" + firebaseAuth.getCurrentUser().getEmail() + "\n";
        userdetails.setText(userdetailstxt);
    }
}
