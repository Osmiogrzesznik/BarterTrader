package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ChatRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ChatRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageThreadDataModel;

import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageThreadDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatRVAdapter.OnItemClickListener {
    private UserDataModel me;   // me is the user who is running this client application - this is easier way to wrap head around all reciprocity
    private UserDataModel you; // you is the user on the other side of the server
    private String myID;
    private String yourID;
    private String messageThreadID;
    private boolean msgThreadExists;
    private MessageThreadDataModel messageThread;// todo you have to update it every time message is send
    private DatabaseReference db_msgThrRef;
    private DatabaseReference db_messagesRef;
    private Toolbar barterBar;
    private CircleImageView ivLeftmostimgToolbar;
    private CircleImageView ivProfilepicToolbar;
    private ImageView ivInboxIconToolbar;
    private TextView tvUnreadThreadsAmountCircle;
    private TextView tvMainTextToolbar;
    private CircleImageView ivSendMessage;
    private List<MessageDataModel> messages;
    private LinearLayoutManager linearLayoutManager;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;
    private ChatRVAdapter adapter;
    private EditText etMsgBodyInput;

    //Firebase Listeners
    private ValueEventListener keepMarkingAsReadWhileActivityIsOn = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot whoLastIdDS) {
            if (whoLastIdDS.exists()) {
                String whoLastId = whoLastIdDS.getValue(String.class);
                if (!DB.isMe(whoLastId)) {
                    db_msgThrRef.child("read").setValue(true);
                    DB.getMeReference().child("inbox").child(messageThreadID).setValue(false);//unread? false
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private ValueEventListener keepRefreshingAdapterWithNewMessages = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot messagesDS) {
            msgThreadExists = messagesDS.exists();
            if (!msgThreadExists) {
                Toast.makeText(ChatActivity.this, "no thread we have to create one just before sending your 1st message ", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(ChatActivity.this, "thread exists hurray!", Toast.LENGTH_LONG).show();
            messages.clear();
            for (DataSnapshot messageDS : messagesDS.getChildren()) {
                MessageDataModel message = messageDS.getValue(MessageDataModel.class);
                message.key = messageDS.getKey();
                messages.add(message);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private View.OnClickListener showReviewsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChatActivity.this, ReviewsActivity.class);
            intent.putExtra("uuid", yourID);
            startActivity(intent);
        }
    };


    @Override
    public void onItemClick(final MessageDataModel clickedMessage, int itemViewType) {

        //only if inboxElement had finished loading form fb, and only if it is the offer of the other user (not fair to accept your own offer)
        //since data is agnostic to who is viewing and to avoid calcuclating again we may use itemViewType
        if (clickedMessage.isComplete() && itemViewType == ChatRVAdapter.YOUR_OFFER) {
            final ItemData finalMyItem = clickedMessage.getWantedItem();
            final ItemData finalYourItem = clickedMessage.getOfferedItem();
            Toast.makeText(ChatActivity.this, "you clicked" + clickedMessage.toString(), Toast.LENGTH_LONG).show();

            if (finalMyItem == null || finalYourItem == null) {
                Toast.makeText(ChatActivity.this, "one of items is already sold and was not removed from db", Toast.LENGTH_LONG).show();
                return;
            }
            BarterTradeConfirmationDialog dialog = new BarterTradeConfirmationDialog(ChatActivity.this, "Trade these items?", finalMyItem, finalYourItem);
            dialog.setOnOKClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                            showFinishedTradePopup(finalMyItem,finalYourItem);
                    finalizeTrade(finalMyItem, finalYourItem, clickedMessage);
                }
            });
            dialog.show();

        }
    }



    private void finalizeTrade(final ItemData myItem, final ItemData yourItem, MessageDataModel clickedMessage) {
        String newBody = you.getFullName() + "'s offer: \n " + yourItem.getTitle() + "\n for \n " + me.getFullName() + "'s \n" + myItem.getTitle() + "\n was \n ACCEPTED ";
        MessageDataModel msgToNotifyYouAboutAccepting = MessageDataModel.makeNormalMessage(yourID, newBody);
        String newMessageID = db_messagesRef.push().getKey();

        //use map to update every node at once and call only one success listener
        Map<String, Object> map = new HashMap<>();

        // set tradedWith of both users to each other
        map.put("/users/" + myID + "/tradedWith/" + yourID + "/", true);
        map.put("/users/" + yourID + "/tradedWith/" + myID + "/", true);


        //mark as agreed and change the clicked message body to keep some way of archive of previously traded items for user
        map.put("/messageThreads_messages/" + messageThreadID + "/" + clickedMessage.key + "/agreed/", true);
        map.put("/messageThreads_messages/" + messageThreadID + "/" + clickedMessage.key + "/body/", newBody);
        map.put("/messageThreads_messages/" + messageThreadID + "/" + clickedMessage.key + "/offer/", false);

        // to actually visualise ad notify that "Me" took some action - otherwise accepting some old offer would be really confusing for user who made that offer
        map.put("/messageThreads_messages/" + messageThreadID + "/" + newMessageID, msgToNotifyYouAboutAccepting);

        //update messageThread header so other user sees acceptance in his inbox
        MessageThreadDataModel msgThread = new MessageThreadDataModel("ACCEPTED OFFER");
        map.put("/messageThreads/" + messageThreadID, msgThread);
        // redundant way of notifying second user he has unread message in his inbox from dashboard (to not load message thread itself in dashboard)
        map.put("/users/" + yourID + "/inbox/" + messageThreadID + "/", true);
        //        map.put("/messageThreads/" + messageThreadID + "/read/", false);
        //        map.put("/messageThreads/" + messageThreadID + "/lastMessageBody/", "ACCEPTED OFFER");
        //        map.put("/messageThreads/" + messageThreadID + "/whoWroteLast/", DB.myUid());
        //        map.put("/messageThreads/" + messageThreadID + "/lastMessageTime/", ServerValue.TIMESTAMP);


        //remove traded items from lists : items, itemsbyuser , categories
        map.put("/items/" + myItem.getId() + "/", null);
        map.put("/items/" + yourItem.getId() + "/", null);
        map.put("/items_by_user/" + myItem.getId() + "/", null);
        map.put("/items_by_user/" + yourItem.getId() + "/", null);
        map.put("/categories/" + myItem.getCategory() + "/" + myItem.getId() + "/", null);
        map.put("/categories/" + yourItem.getCategory() + "/" + yourItem.getId() + "/", null);


        // todo some offer messages still referring to sold and removed item from your messageThreads should RecyclerViewAdapter actively react to offers with null items?
        // todo 2 it is a little bit complicated to search through all other threads.

        DB.root.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showFinishedTradePopup(myItem, yourItem);
            }
        });

    }

    private void showFinishedTradePopup(ItemData myItem, ItemData yourItem) {
        BarterTradeConfirmationDialog d = new
                BarterTradeConfirmationDialog(this,
                "You Have Succesfully traded with " + you.getFullName(), myItem, yourItem);
        d.isTradeFinalized(true);
        d.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        barterBar = findViewById(R.id.BarterBar);

        DB.getMeReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(UserDataModel.class);
//                Log.d("BOLO", me.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "could not load ", Toast.LENGTH_SHORT).show();
            }
        });

        //Toolbar UI
        ivLeftmostimgToolbar = barterBar.findViewById(R.id.iv_leftMostImg_toolbar);
        ivProfilepicToolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        ivInboxIconToolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
        tvUnreadThreadsAmountCircle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        tvMainTextToolbar = barterBar.findViewById(R.id.tv_mainText_toolbar);

        //Chat UI
        recyclerView = findViewById(R.id.rv_chat);
        ivSendMessage = findViewById(R.id.iv_sendMessage);
        etMsgBodyInput = findViewById(R.id.et_msgBodyInput_chat);

        ivProfilepicToolbar.setVisibility(View.GONE);
        ivInboxIconToolbar.setVisibility(View.GONE);
        tvUnreadThreadsAmountCircle.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(this);
linearLayoutManager.setStackFromEnd(true);
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        messages = new ArrayList<>();
        adapter = new ChatRVAdapter(mInflater, messages);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        //

        Intent intentThatStartedChat = getIntent();
        you = (UserDataModel) intentThatStartedChat.getSerializableExtra("otherUser");
        if (you == null){
            throw new NullPointerException("ChatActivity started without required otheUser UserDataModel extra");
        }

        if (you.getPicture() != null) {
            Picasso.get().load(you.getPicture()).into(ivLeftmostimgToolbar);
        }
        tvMainTextToolbar.setText(you.getFullName());
        ivLeftmostimgToolbar.setOnClickListener(showReviewsListener);
        tvMainTextToolbar.setOnClickListener(showReviewsListener);

        myID = DB.me.getUid();
        yourID = you.getUuid();
        // TODO: 19/11/2019 chck if messagethread exists and open it
        // TODO: 17/11/2019 code chat activity

        messageThreadID = DB.calculateMessageThreadID(myID, yourID);

        db_msgThrRef = DB.messageThreads.child(messageThreadID);
        db_messagesRef = DB.messageThreads_messages.child(messageThreadID);

        ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });



        db_messagesRef.addValueEventListener(keepRefreshingAdapterWithNewMessages);
        db_msgThrRef.child("whoWroteLast").addValueEventListener(keepMarkingAsReadWhileActivityIsOn);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (db_msgThrRef != null) {
            db_msgThrRef.removeEventListener(keepMarkingAsReadWhileActivityIsOn);
        }
        if (db_messagesRef != null) {
            db_messagesRef.removeEventListener(keepRefreshingAdapterWithNewMessages);
        }
    }

    private void startListeningForSendingMessages() {


    }

    private void sendMessage() {
        String body = etMsgBodyInput.getText().toString();
        if (!body.trim().isEmpty()) {
            MessageDataModel lastWrittenMessage = MessageDataModel.makeNormalMessage(yourID, body);

            // update OR create thread header for inbox view
            MessageThreadDataModel mtdm = new MessageThreadDataModel(body);
            DB.messageThreads.child(messageThreadID).setValue(mtdm);
            DB.users.child(yourID).child("inbox").child(messageThreadID).setValue(true); // set boolean value to indicate to other user at least one unread message
            // save message in database
            DB.messageThreads_messages.child(messageThreadID).push().setValue(lastWrittenMessage);
            //reset input
            etMsgBodyInput.setText("");
        }
    }


}
