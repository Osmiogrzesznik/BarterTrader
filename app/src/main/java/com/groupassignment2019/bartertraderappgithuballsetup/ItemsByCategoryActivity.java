package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ItemRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsByCategoryActivity extends AppCompatActivity {
    //Listeners
    final ItemRVAdapter.OnItemClickListener clickListener = new ItemRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ItemData clickedItemData) {
            Toast.makeText(ItemsByCategoryActivity.this, "you clicked" + clickedItemData.getTitle(), Toast.LENGTH_LONG).show();
        }
    };

    public static final String BOLO = "BOLO";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private TextView textViewItem_list_NarrowDownTitle;
    private List<ItemData> itemsArray;
    private LayoutInflater mInflater;
    private ItemRVAdapter adapter;
    private DatabaseReference mRootReference;
    private DatabaseReference mDBCategoryNameRef;
    private TextView textViewDEBUG_FIREBASE;
    private DatabaseReference DB_Items_reference;
    private ValueEventListener addItemToListOnValueEvent;
    private String byWhat_UserOrCategory;
    private Intent intentThatStartedMe;
    private String userIdOrCategoryName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.categories_menu, menu);
        MenuItem searchItemDataItem = menu.findItem(R.id.action_search);
        SearchView searchView = (androidx.appcompat.widget.SearchView) searchItemDataItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category);
        setTitle("Choose one of items");

        //Views Set Up
        textViewDEBUG_FIREBASE = findViewById(R.id.textViewDEBUG_FIREBASE);
        textViewItem_list_NarrowDownTitle = findViewById(R.id.Item_list_NarrowDownTitle);
        recyclerView = findViewById(R.id.itemsRecyclerView);


        //FIREBASE VARIABLES SET UP
        mRootReference = FirebaseDatabase.getInstance().getReference();
        DB_Items_reference = mRootReference.child("items");


        itemsArray = new ArrayList<>();

        //prepare RecyclerView
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(clickListener);
        adapter = new ItemRVAdapter(mInflater, itemsArray);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(clickListener);

        //Recover info sent from previous activity
        intentThatStartedMe = getIntent();

        checkWhatTypeOfListUserShouldSee();

        if (userShouldSeeItemsInCategory()) {
            setUpAdapterToBeFilledWithItemsFromCategory();
        } else if (userShouldSeeItemsByOtherUser()) {
            // setUpAdapterToBeFilledWithItemsByOtherUser();
        }


    }

    private void setUpAdapterToBeFilledWithItemsByOtherUser() {
        String UUID = null;
        FirebaseUser firebaseUser;
        UserDataModel user;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String filterValue = intentThatStartedMe.getExtras().getString(byWhat_UserOrCategory);//if filter Type was user it will get UUID


        if (filterValue.contentEquals(firebaseUser.getUid())) {

            boolean isOwner = true;
            textViewItem_list_NarrowDownTitle.setText("Items By Me");
            recyclerView.setHasFixedSize(false);
            // mRootReference.child("items_by_user")
        } else {
            boolean isOwner = false;
            textViewItem_list_NarrowDownTitle.setText("Items By User");
            recyclerView.setHasFixedSize(true);
            //user = mRootReference.child("users").child(UUID).addListenerForSingleValueEvent();
        }
        UUID = firebaseUser.getUid();
        textViewItem_list_NarrowDownTitle.setText("Other Items by user :" + filterValue.toUpperCase());
        Toast.makeText(ItemsByCategoryActivity.this, filterValue, Toast.LENGTH_LONG).show();
    }

    private void setUpAdapterToBeFilledWithItemsFromCategory() {
        //if filter Type was category it will get category title
        userIdOrCategoryName = intentThatStartedMe.getExtras().getString(byWhat_UserOrCategory);
        textViewItem_list_NarrowDownTitle.setText("Items by " + byWhat_UserOrCategory + " :" + userIdOrCategoryName.toUpperCase());
        Toast.makeText(ItemsByCategoryActivity.this, userIdOrCategoryName, Toast.LENGTH_LONG).show();
        // items = new ArrayList<>();
        Log.d(BOLO, "starting" + byWhat_UserOrCategory + " " + userIdOrCategoryName.toLowerCase());
        mDBCategoryNameRef = FirebaseDatabase.getInstance().getReference("categories").child(userIdOrCategoryName.toLowerCase());


        //this will happen for every itemID
        addItemToListOnValueEvent = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemData item = dataSnapshot.getValue(ItemData.class);
                itemsArray.add(item);
                adapter.notifyDataSetChanged();
                //here somthing should happen to inform adapter that something has changed
                Log.d(BOLO, item.getTitle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemsByCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        };

        //this gets a list of itemIds under the selected category
        mDBCategoryNameRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemsArray.clear();
                        for (DataSnapshot itemID : dataSnapshot.getChildren()) {
                            String itemIDKey = itemID.getKey();
                            Log.d(BOLO, itemIDKey);
                            //for each of ids sent request for adding actual item
                            DB_Items_reference.child(itemIDKey).addValueEventListener(addItemToListOnValueEvent);
                        }

                        // either itemsArray should be observable (and adapter should know about it or something else should happen
                        // see adapterListItemAdder



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(BOLO, databaseError.getMessage());
                        Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        );
    }

    private void checkWhatTypeOfListUserShouldSee() {
        byWhat_UserOrCategory = intentThatStartedMe.getExtras().getString("by");
        if (byWhat_UserOrCategory == null)
            throw new NullPointerException("BT: user should be redirected here with data- there is no data passed in the intent");
    }

    private boolean userShouldSeeItemsInCategory() {
        if (byWhat_UserOrCategory == null) {
            checkWhatTypeOfListUserShouldSee();
        }
        return byWhat_UserOrCategory.equalsIgnoreCase("category");
    }

    private boolean userShouldSeeItemsByOtherUser() {
        if (byWhat_UserOrCategory == null) {
            checkWhatTypeOfListUserShouldSee();
        }
        return byWhat_UserOrCategory.equalsIgnoreCase("uuid");
    }

    private void fetchByitemId(String itemIDAsString) {
        mRootReference.child("items").child(itemIDAsString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ItemData itemData = dataSnapshot.getValue(ItemData.class);
                        itemsArray.add(itemData);
                        Log.d("BOLO", itemData.toString());
//
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ItemsByCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void DB(String UUID) {

        mRootReference.child("items_by_user").child(UUID).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemID : snapshot.getChildren()) {
                    mRootReference.child("items").child(itemID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot ids) {
                            // System.out.println(lectureSnapshot.child("title").getValue());
                            ItemData itda = ids.getValue(ItemData.class);
                            itemsArray.add(itda);

                            Log.d(BOLO, itda.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(ItemsByCategoryActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                adapter = new ItemRVAdapter(mInflater, itemsArray);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(clickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
