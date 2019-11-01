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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ItemRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;

import java.util.ArrayList;
import java.util.List;

public class ItemsByCategoryActivity extends AppCompatActivity {

    private List<ItemData> items;
    private LayoutInflater mInflater;
    private ItemRVAdapter adapter;
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
//        TextView tv = findViewById(R.id.CategoryNameTextView);
        setContentView(R.layout.activity_items_by_category);
        setTitle("Choose one of items");

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
//            tv.setText("ItemData: " + filterValue.toUpperCase());
            Toast.makeText(ItemsByCategoryActivity.this, filterValue, Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(filterValue.toLowerCase());

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for (DataSnapshot itemsDataSnapshot: dataSnapshot.getChildren()){

    ItemData itemData = itemsDataSnapshot.getValue(ItemData.class);
    items.add(itemData);
    Log.d("BOLO",itemData.toString());
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


    }


}
