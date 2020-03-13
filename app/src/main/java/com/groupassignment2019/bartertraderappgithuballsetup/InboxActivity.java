package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.IdentifiableValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.InboxElementRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.InboxElement;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class InboxActivity extends AppCompatActivity {
    //Listeners
    final InboxElementRVAdapter.OnItemClickListener clickListener = new InboxElementRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(InboxElement clickedinboxElement) {
            if (clickedinboxElement.isComplete()){ //only if inboxElement had finished loading form fb
                Toast.makeText(InboxActivity.this, "you clicked" + clickedinboxElement.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
            intent.putExtra("otherUser", clickedinboxElement.getUserInterlocutor());
            startActivity(intent);
        }
        };
    };


    public static final String BOLO = "BOLO";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private List<InboxElement> inboxElementList;
    private LayoutInflater mInflater;
    private InboxElementRVAdapter adapter;
//    private IdentifiableValueEventListener<InboxElement> addInboxElementToAdapterWhenSentFromDB;
    private FirebaseUser firebaseUser;
    private DatabaseReference DB_listOfThreadsInbox;
    private TextView tvIfEmpty;
    private DatabaseReference DB_threadHeaders;
    private ChildEventListener addInboxElementToAdapterWhenSentFromDBCEL;

    private CircleImageView ivAddReview;



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
        tvIfEmpty = (TextView) findViewById(R.id.tv_ifEmpty);
        ivAddReview = (CircleImageView) findViewById(R.id.iv_AddReview);



        //Views Set Up
        recyclerView = findViewById(R.id.inbox_recyclerView);

        //FIREBASE VARIABLES SET UP
        DB_listOfThreadsInbox = DB.users.child(firebaseUser.getUid()).child("inbox");
        DB_threadHeaders = DB.messageThreads;

        inboxElementList = new ArrayList<>();

        //prepare RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new InboxElementRVAdapter(mInflater, inboxElementList, DB.users);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(clickListener);



//                new IdentifiableValueEventListener<InboxElement>(this,adapter, InboxElement.class,0);


        // TODO: 11/11/2019 in adapter oncreateviuew holder load urls
        //this will make above happen for every itemID



        //get the list of threads from user inbox node


    }

    @Override
    protected void onStart() {
        super.onStart();
//        DB_listOfThreadsInbox.addValueEventListener(keepListentingToChangesInInbox);
        DB_listOfThreadsInbox.addChildEventListener(keepListentingToChangesInInboxCEL);
        tvIfEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        DB_listOfThreadsInbox.removeEventListener(keepListentingToChangesInInbox);
        DB_listOfThreadsInbox.removeEventListener(keepListentingToChangesInInboxCEL);
    }

    // Listeners
    private ChildEventListener keepListentingToChangesInInboxCEL = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot msgThrDS, @Nullable String s) {
            tvIfEmpty.setVisibility(View.GONE);
            final String msgThrIDKey = msgThrDS.getKey();
            final String otheruserId = msgThrIDKey.replace(firebaseUser.getUid(),"");
            Log.d(BOLO, msgThrIDKey);
            //for each of ids send request for adding actual item
            DB_threadHeaders.child(msgThrIDKey).addValueEventListener(listen_to_changes_in_ONE_InboxElement);

        // either itemsArray should be observable (and adapter should know about it or something else should happen
        // see adapterListItemAdder
    }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            adapter.thatInboxElementisUnread(dataSnapshot.getKey(),);
            //no need to listen for updates of this boolean value here (only in dashboard it matters)
            //warning this will be changing constantly together with read indicator in msgthreads
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            //never happens
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //never happens
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(BOLO, databaseError.getMessage());
            Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

//    private ValueEventListener keepListentingToChangesInInbox = new ValueEventListener() {
//        //GRAB INBOX OF USER
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            if (!dataSnapshot.exists()) { // nothing in inbox yet - new users
//                tvIfEmpty.setVisibility(View.VISIBLE);
//            }
//            else {
//                tvIfEmpty.setVisibility(View.GONE);
//            }
//
//            inboxElementList.clear();
//            adapter.notifyDataSetChanged();
//            for (DataSnapshot msgThrID : dataSnapshot.getChildren()) {
//                final String msgThrIDKey = msgThrID.getKey();
//                final String otheruserId = msgThrIDKey.replace(firebaseUser.getUid(),"");
//                Log.d(BOLO, msgThrIDKey);
//                //for each of ids send request for adding actual item
//                //Before adding the inboxElement to Adapter we set fields required to grab other user picture and so on
//                IdentifiableValueEventListener.OnBeforeAddedListener<InboxElement> onBeforeAddedListener = new IdentifiableValueEventListener.OnBeforeAddedListener<InboxElement>(){
//                    @Override
//                    public InboxElement OnBeforeAdded(InboxElement element,int identifier) {
//                        element.setMessageThreadId(msgThrIDKey);
//                        element.setOtherUserID(otheruserId);
//                        return element;
//                    }
//                };
//                //get fresh local copy to avoid using all new onbeforeAddedListeners on one Object, therefore loosing msgThrdIDKey and otherUserId
//                IdentifiableValueEventListener adder = listen_to_changes_in_ONE_InboxElement.copy(0);
//                adder.setOnBeforeAddedListener(onBeforeAddedListener);
//                DB.messageThreads.child(msgThrIDKey).addValueEventListener(adder);
//            }
//
//            // either itemsArray should be observable (and adapter should know about it or something else should happen
//            // see adapterListItemAdder
//
//
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//            Log.e(BOLO, databaseError.getMessage());
//            Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };



    private ValueEventListener listen_to_changes_in_ONE_InboxElement = new ValueEventListener (){
        @Override
        public void onDataChange(@NonNull DataSnapshot messageThreadDS) {
            String msgThrIDKey = messageThreadDS.getKey();
            if (!messageThreadDS.exists()){
                adapter.removeInboxElement(msgThrIDKey);
            }

            String otherUserId = msgThrIDKey.replace(firebaseUser.getUid(),"");
            //Before adding the inboxElement to Adapter we set fields required to grab other user picture and so on
            InboxElement inboxElement = messageThreadDS.getValue(InboxElement.class);
            inboxElement.setMessageThreadId(msgThrIDKey);
            inboxElement.setOtherUserID(otherUserId);
            adapter.addOrUpdateInboxElement(msgThrIDKey,inboxElement);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
