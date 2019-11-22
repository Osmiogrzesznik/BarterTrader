package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.InboxElementRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.MessageDataModelRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.InboxElement;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    final MessageDataModelRVAdapter.OnItemClickListener clickListener = new MessageDataModelRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(MessageDataModel clickedMessage) { // TODO: 21/11/2019 should accept the itemViewType to know whteher normal message or offer was clicked
            if (clickedMessage.getType().equalsIgnoreCase(MessageDataModel.TYPE_OFFER)){ //only if inboxElement had finished loading form fb
                Toast.makeText(ChatActivity.this, "you clicked" + clickedMessage.toString(), Toast.LENGTH_LONG).show();
            }
        };
    };
    private Toolbar barterBar;
    private CircleImageView ivLeftmostimgToolbar;
    private CircleImageView ivProfilepicToolbar;
    private ImageView ivInboxIconToolbar;
    private TextView tvUnreadThreadsAmountCircle;
    private TextView tvMainTextToolbar;
    private UserDataModel you;
    private CircleImageView ivSendMessage;
    private List<MessageDataModel> messages;
    private boolean NoEarlierConversation;
    private LinearLayoutManager linearLayoutManager;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;
    private MessageDataModelRVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        barterBar = findViewById(R.id.BarterBar);
        //Toolbar UI
        ivLeftmostimgToolbar = barterBar.findViewById(R.id.iv_leftMostImg_toolbar);
        ivProfilepicToolbar = barterBar.findViewById(R.id.iv_profilepic_toolbar);
        ivInboxIconToolbar = barterBar.findViewById(R.id.iv_inbox_icon_toolbar);
        tvUnreadThreadsAmountCircle = barterBar.findViewById(R.id.tv_unreadThreadsAmount_circle);
        tvMainTextToolbar = barterBar.findViewById(R.id.tv_mainText_toolbar);

        //Chat UI
        recyclerView = findViewById(R.id.rv_chat);
        ivSendMessage = findViewById(R.id.iv_sendMessage);

        ivProfilepicToolbar.setVisibility(View.GONE);
        ivInboxIconToolbar.setVisibility(View.GONE);
        tvUnreadThreadsAmountCircle.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(this);
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        messages = new ArrayList<>();
        adapter = new MessageDataModelRVAdapter(mInflater, messages);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(clickListener);

        //

        Intent intentThatStartedChat = getIntent();
        you = (UserDataModel) intentThatStartedChat.getSerializableExtra("otherUser");
            updateUI();

        String MyID = DB.currentUser.getUid();
        String yourID = you.getUuid();
        // TODO: 19/11/2019 chck if messagethread exists and open it
        // TODO: 17/11/2019 code chat activity

        String mtid = DB.calculateMessageThreadID(MyID,yourID);
        DB.messageThreads_messages.child(mtid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot listOfMessagesDataSnapshot) {
                if (listOfMessagesDataSnapshot.exists()){
                    Toast.makeText(ChatActivity.this, "thread exists hurray!", Toast.LENGTH_LONG).show();
                    NoEarlierConversation = true;
                    messages.clear();
                    for (DataSnapshot messageSnap: listOfMessagesDataSnapshot.getChildren()){
                        MessageDataModel message = messageSnap.getValue(MessageDataModel.class);
                        messages.add(message);
                    }
                    adapter.notifyDataSetChanged();
                    onMessagesLoaded();
                }
                // TODO: 19/11/2019 open thread in chat activity
                else{
                    Toast.makeText(ChatActivity.this, "no thread we have to create one before sending message ", Toast.LENGTH_LONG).show();
                    NoEarlierConversation = false;
                }
                startListeningForSendingMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startListeningForSendingMessages() {
        ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {

    }


    private void onMessagesLoaded() {

    }




    public void updateUI() {
        Picasso.get().load(you.getPicture()).into(ivLeftmostimgToolbar);
        tvMainTextToolbar.setText(you.getFullName());
    }

}
