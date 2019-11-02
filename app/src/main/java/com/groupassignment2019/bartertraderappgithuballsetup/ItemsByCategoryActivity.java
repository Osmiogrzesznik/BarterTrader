package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsByCategoryActivity extends AppCompatActivity {
    private TextView textViewItem_list_NarrowDownTitle;
    private List<ItemData> items;
    private LayoutInflater mInflater;
    private ItemRVAdapter adapter;
    private DatabaseReference mRootReference;
    private DatabaseReference mDatabaseRef;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category);
        setTitle("Choose one of items");
        textViewItem_list_NarrowDownTitle = findViewById(R.id.Item_list_NarrowDownTitle);
        mRootReference = FirebaseDatabase.getInstance().getReference();

        mInflater = LayoutInflater.from(this.getBaseContext());
        final RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);
        final ItemRVAdapter.OnItemClickListener clickListener = new ItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemData clickedItemData) {
                Toast.makeText(ItemsByCategoryActivity.this, "you clicked" + clickedItemData.getTitle(), Toast.LENGTH_LONG).show();
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));


        Intent intent_that_started_me = getIntent();
        String filterType = intent_that_started_me.getExtras().getString("by");
        if (filterType.equalsIgnoreCase("category")) {
            String filterValue = intent_that_started_me.getExtras().getString(filterType);//if filter Type was category it will get category title
            textViewItem_list_NarrowDownTitle.setText("Items by " + filterType + " :" + filterValue.toUpperCase());
            Toast.makeText(ItemsByCategoryActivity.this, filterValue, Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(filterValue.toLowerCase());

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot itemsDataSnapshot : dataSnapshot.getChildren()) {
                        ItemData itemData = itemsDataSnapshot.getValue(ItemData.class);
                        items.add(itemData);
                        Log.d("BOLO", itemData.toString());
                    }
                    adapter = new ItemRVAdapter(mInflater, items);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(clickListener);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ItemsByCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        else if(filterType.equalsIgnoreCase("uuid")){
            String UUID = null;
            FirebaseUser firebaseUser;
            UserDataModel user;

            String filterValue = intent_that_started_me.getExtras().getString(filterType);//if filter Type was user it will get UUID


            if(filterValue.contentEquals("me")){
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //String UUID = firebaseUser.getUid();
                boolean isOwner = true;
                textViewItem_list_NarrowDownTitle.setText("Items By Me");
            }else{
                //user = mRootReference.child("users").child(UUID).addListenerForSingleValueEvent();
            }
            textViewItem_list_NarrowDownTitle.setText("Other Items by user :" + filterValue.toUpperCase());
            Toast.makeText(ItemsByCategoryActivity.this, filterValue, Toast.LENGTH_LONG).show();

        }


    }

    private void DB(String UUID){

        mRootReference.child("items_by_user").child(UUID).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot lessonKey: snapshot.getChildren()) {
                    mRootReference.child("").child(lessonKey.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot lectureSnapshot) {
                            System.out.println(lectureSnapshot.child("title").getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            
                        }

                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(ItemsByCategoryActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void onListOfItemsDownloaded(){

    }


}
