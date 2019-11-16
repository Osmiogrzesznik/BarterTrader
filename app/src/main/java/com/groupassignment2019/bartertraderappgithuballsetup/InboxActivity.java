package com.groupassignment2019.bartertraderappgithuballsetup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.AddElementToAdapterValueListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.InboxElementRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.InboxElement;


import java.util.ArrayList;
import java.util.List;


public class InboxActivity extends AppCompatActivity {
    //Listeners
    final InboxElementRVAdapter.OnItemClickListener clickListener = new InboxElementRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(InboxElement clickedinboxElement) {
            Toast.makeText(InboxActivity.this, "you clicked" + clickedinboxElement.toString(), Toast.LENGTH_LONG).show();
        }
    };


    public static final String BOLO = "BOLO";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private List<InboxElement> inboxElementList;
    private LayoutInflater mInflater;
    private InboxElementRVAdapter adapter;
    private AddElementToAdapterValueListener<InboxElement> addInboxElementToAdapterWhenSentFromDB;
    private ValueEventListener grabListOfMTIDsandprepareneededdata;
    private FirebaseUser firebaseUser;
    private DatabaseReference DB_listOfThreadsInbox;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(this, "user is not logged in", Toast.LENGTH_LONG).show();
            finish();
        }

        //Views Set Up
        recyclerView = findViewById(R.id.inbox_recyclerView);

        //FIREBASE VARIABLES SET UP
        DB_listOfThreadsInbox = DB.users.child(firebaseUser.getUid()).child("inbox");

        inboxElementList = new ArrayList<>();

        //prepare RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new InboxElementRVAdapter(mInflater, inboxElementList, DB.users);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(clickListener);

        //Recover info sent from previous activity
        addInboxElementToAdapterWhenSentFromDB = new AddElementToAdapterValueListener<InboxElement>(this,adapter, InboxElement.class);


        // TODO: 11/11/2019 in adapter oncreateviuew holder load urls
        //this will make above happen for every itemID
        grabListOfMTIDsandprepareneededdata = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxElementList.clear();

                for (DataSnapshot msgThrID : dataSnapshot.getChildren()) {
                    final String msgThrIDKey = msgThrID.getKey();
                    final String otheruserId = msgThrIDKey.replace(firebaseUser.getUid(),"");
                    Log.d(BOLO, msgThrIDKey);
                    //for each of ids send request for adding actual item
                    //Before adding the inboxElement to Adapter we set fields required to grab other user picture and so on
                    AddElementToAdapterValueListener.OnBeforeAddedListener<InboxElement> onBeforeAddedListener = new AddElementToAdapterValueListener.OnBeforeAddedListener<InboxElement>(){
                        @Override
                        public InboxElement OnBeforeAdded(InboxElement element) {
                            element.setMessageThreadId(msgThrIDKey);
                            element.setOtherUserID(otheruserId);
                            return element;
                        }
                    };
                    //get fresh local copy to avoid using all new onbeforeAddedListeners on one Object, therefore loosing msgThrdIDKey and otherUserId
                    AddElementToAdapterValueListener adder = addInboxElementToAdapterWhenSentFromDB.copy();
                    adder.setOnBeforeAddedListener(onBeforeAddedListener);
                    DB.messageThreads.child(msgThrIDKey).addValueEventListener(adder);
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


        //get the list of threads from user inbox node
        DB_listOfThreadsInbox.addValueEventListener(grabListOfMTIDsandprepareneededdata);

    }


}
