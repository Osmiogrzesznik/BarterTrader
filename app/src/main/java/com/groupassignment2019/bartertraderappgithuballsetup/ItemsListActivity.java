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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ItemRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsListActivity extends AppCompatActivity {


    public static final String BOLO = "BOLO";
    private static final String CATEGORY = "category";
    private static final String OFFER = "offer";
    private static final String BY = "by";
    private static final String USER_ID = "uuid";
    private ChildEventListener CELgrabListOfIdsAndFindActualItems;
    private DatabaseReference DB_Dynamic_ItemsIDListWeObserve_ref;


    /**
     * sets the intent extras to start ItemsListActivity with valid input extras adhering to the contract
     * @param intent intent that will be used to start ItemsListActivity
     */
    public static void BY_ME(Intent intent){
        BY_USER(intent,DB.me.getUid());
    }

    /**
     * sets the intent extras to start ItemsListActivity with valid input extras adhering to the contract
     * @param intent intent that will be used to start ItemsListActivity
     * @param uuid uid of user whose items will be displayed
     */
    public static void BY_USER(Intent intent, String uuid){
        intent.putExtra(BY, USER_ID);
        intent.putExtra(USER_ID,uuid);
    }
// TODO: 20/11/2019 remove actionbar and use custom one as in DashboardActivity
    /**
     * sets the intent extras to start ItemsListActivity with valid input extras adhering to the contract
     * @param intent intent that will be used to start ItemsListActivity
     * @param category uid of category whose items will be displayed
     */
    public static void BY_CATEGORY(Intent intent, String category){
        intent.putExtra(BY,CATEGORY);
        intent.putExtra(USER_ID,category);
    }

    private TextView textViewItem_list_NarrowDownTitle;
    private List<ItemData> itemsArrayList;
    private ItemRVAdapter adapter;
    private DatabaseReference DB_Root_ref;
    private DatabaseReference DB_Items_reference;
    private ValueEventListener addItemToListOnValueEvent;
    private String byWhat_UserOrCategory;
    private Intent intentThatStartedMe;
    private ValueEventListener grabListOfIdsAndFindActualItems;
    private String mode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_toolbar_menu, menu);
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
        setContentView(R.layout.activity_items_list);
        setTitle("Choose one of items");

        //Views Set Up
        textViewItem_list_NarrowDownTitle = findViewById(R.id.Item_list_NarrowDownTitle);
        RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);


        //FIREBASE VARIABLES SET UP
        DB_Root_ref = FirebaseDatabase.getInstance().getReference();
        DB_Items_reference = DB_Root_ref.child("items");




        //prepare RecyclerView
        /**
         * sets the intent extras to start ItemsListActivity with valid input extras adhering to the contract
         * @param intent intent that will be used to start ItemsListActivity
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LayoutInflater mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        itemsArrayList = new ArrayList<>();
        adapter = new ItemRVAdapter(mInflater, itemsArrayList);

        recyclerView.setAdapter(adapter);

        //Recover info sent from previous activity
        intentThatStartedMe = getIntent();
        checkWhatTypeOfListUserShouldSee();
        adapter.setOnItemClickListener(itemClickListener);


        //this will happen for every itemID
        addItemToListOnValueEvent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemDS) {
                String key = itemDS.getKey();
                if (!itemDS.exists()){
                    adapter.removeItemData(itemDS.getKey());
                    //this is only to repair data in db REMOVE IT BEFORE SUBMITTING
                    // todo remove it or keep it on gard for data consistency __ REMOVE because it will fire the value event every time for nuls starting everything again
                    DB_Dynamic_ItemsIDListWeObserve_ref.child(key).removeValue();
                    return;
                }
                ItemData item = itemDS.getValue(ItemData.class);
                item.setId(itemDS.getKey());
                adapter.addOrModifyItemData(itemDS.getKey(),item);
                //here somthing should happen to inform consumer that something has changed
                Log.d("BOLO", item.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemsListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
//        addItemToListOnValueEventOLD = new IdentifiableValueEventListener<ItemData>(this, adapter, ItemData.class, -1);


//                new IdentifiableValueEventListener<ItemData>(this, adapter, ItemData.class, -1);

        //this will make above happen for every itemID



        grabListOfIdsAndFindActualItems = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsArrayList.clear();
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
        };


        if (userShouldSeeItemsInCategory()) {
            setUpAdapterToBeFilledWithItemsFromCategory();
        } else if (userShouldSeeItemsByOtherUser()) {
             setUpAdapterToBeFilledWithItemsByOtherUser();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        itemsArrayList.clear();//items should be cashed locally anyway
        DB_Dynamic_ItemsIDListWeObserve_ref.addListenerForSingleValueEvent(grabListOfIdsAndFindActualItems);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DB_Dynamic_ItemsIDListWeObserve_ref.removeEventListener(grabListOfIdsAndFindActualItems);
    }

    private void setUpAdapterToBeFilledWithItemsByOtherUser() {
        String UUID = null;
        boolean isOwner;
        FirebaseUser firebaseUser;
        UserDataModel user;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null ){
            // TODO: 18/11/2019 !!??? what the fuck?
//            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com","password") ;
//            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            throw new NullPointerException("BT: BOLO : NO USER LOGGED IN FOR VIEWING ITEMS. This activity requires user to be logged in but not really");
        }
        String user_id_from_intent = intentThatStartedMe.getExtras().getString(USER_ID);//if filter Type was user it will get UUID


        if (user_id_from_intent.contentEquals(firebaseUser.getUid())) {
            // BY_ME scenario

            isOwner = true;
            if (activityWasStartedForResult()){
                textViewItem_list_NarrowDownTitle.setText("My items");
                setTitle("Select item you offer");

            }
            textViewItem_list_NarrowDownTitle.setText("My Items");
            setTitle("My items");
            UUID = firebaseUser.getUid();

        } else {
            // BY ANY OTHER USER scenario

            isOwner = false;
            textViewItem_list_NarrowDownTitle.setText("Items By User");
                UUID = user_id_from_intent;
        }
        if (mode.equals(OFFER)){
            adapter.setIsViewerTheOwner(false);// do not allow user to edit items before picking an offer
        }else{
            adapter.setIsViewerTheOwner(isOwner);
        }

        DatabaseReference DB_itemIDsofItemsByUser_ref = DB_Root_ref.child("items_by_user").child(UUID);
        DB_Dynamic_ItemsIDListWeObserve_ref = DB_itemIDsofItemsByUser_ref;
        //DB_itemIDsofItemsByUser_ref.addValueEventListener(grabListOfIdsAndFindActualItems);
        Toast.makeText(ItemsListActivity.this, user_id_from_intent, Toast.LENGTH_LONG).show();
    }

    /**
     * simple check if activity was started for result (like file selection)
     * @return boolean
     */
    private boolean activityWasStartedForResult() {
        return getCallingActivity() != null;// this is not null only in the case when activity was started for result
    }

    private void setUpAdapterToBeFilledWithItemsFromCategory() {
        //if filter Type was category it will get category title
        String userIdOrCategoryName = intentThatStartedMe.getExtras().getString(byWhat_UserOrCategory);
        textViewItem_list_NarrowDownTitle.setText("Items by " + byWhat_UserOrCategory + " :" + userIdOrCategoryName.toUpperCase());
        Toast.makeText(ItemsListActivity.this, userIdOrCategoryName, Toast.LENGTH_LONG).show();
        // items = new ArrayList<>();
        Log.d(BOLO, "starting" + byWhat_UserOrCategory + " " + userIdOrCategoryName.toLowerCase());
        DatabaseReference mDBListOfItemIdsInCategoryNameRef = FirebaseDatabase.getInstance().getReference("categories").child(userIdOrCategoryName.toLowerCase());
        //this gets a list of itemIds under the selected category
        DB_Dynamic_ItemsIDListWeObserve_ref = mDBListOfItemIdsInCategoryNameRef;
        //DB_ItemsIDListWeObserve_ref.addValueEventListener(grabListOfIdsAndFindActualItems);
    }


    private void checkWhatTypeOfListUserShouldSee() {
        byWhat_UserOrCategory = intentThatStartedMe.getExtras().getString(BY);
        if (byWhat_UserOrCategory == null)
            throw new NullPointerException("BT: user should be redirected here with data- there is no data passed in the intent");
    }

    private boolean userShouldSeeItemsInCategory() {
        if (byWhat_UserOrCategory == null) {
            checkWhatTypeOfListUserShouldSee();
        }
        return byWhat_UserOrCategory.equalsIgnoreCase(CATEGORY);
    }

    private boolean userShouldSeeItemsByOtherUser() {
        if (byWhat_UserOrCategory == null) {
            checkWhatTypeOfListUserShouldSee();
        }
        return byWhat_UserOrCategory.equalsIgnoreCase(USER_ID);
    }




    //Listeners
    private final ItemRVAdapter.OnItemClickListener itemClickListener = new ItemRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ItemData clickedItemData) {
            Toast.makeText(ItemsListActivity.this, "mode:"+ mode + " \nyou clicked" + clickedItemData.getTitle(), Toast.LENGTH_LONG).show();
            Toast.makeText(ItemsListActivity.this, ((Boolean)(mode.equals(OFFER))).toString() , Toast.LENGTH_LONG).show();
            Toast.makeText(ItemsListActivity.this, mode+OFFER , Toast.LENGTH_LONG).show();

            //if mode is offer it means that this activity was started for result so do not start any other activity just finish
            if (/*mode.equalsIgnoreCase(OFFER) */ getCallingActivity() != null){//if was called for result
                Intent intent = new Intent();
                intent.putExtra("item", clickedItemData);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Intent intent = new Intent(ItemsListActivity.this, ItemDetailsActivity.class);
                intent.putExtra("item",clickedItemData);
                startActivity(intent);
            }
        }
    };

}
