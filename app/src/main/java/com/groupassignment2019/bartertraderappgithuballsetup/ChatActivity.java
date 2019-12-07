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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.adapters.ChatRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.MessageThreadDataModel;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    final ChatRVAdapter.OnItemClickListener clickListener = new ChatRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final MessageDataModel clickedMessage, int itemViewType) {
            if (clickedMessage.isComplete()) { //only if inboxElement had finished loading form fb
                ItemData myItem = null;
                ItemData yourItem = null;

                switch (itemViewType) {//since data is agnostic to who is viewing and to avoid calcuclating again we may use itemViewType
                    case ChatRVAdapter.MY_OFFER:
                        myItem = clickedMessage.getOfferedItem();
                        yourItem = clickedMessage.getWantedItem();
                        int backgroundImageDelete;
                        View.OnClickListener promptclicklistener = new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                deleteOffer();
                            }
                        }; //and so on
                        // TODO: 28/11/2019 this case should allow for deletion of made offer; by  changing the picture  in the background
                        //              you can make it indicate (red cross in the middle) that is about to delete the request.
                        // TODO: 27/11/2019 dont do MyOffer user should't be able   to finalize offer he made (what isf the other user dont want the item you propose)
                        return ;
                    case ChatRVAdapter.YOUR_OFFER:
                        yourItem = clickedMessage.getOfferedItem();
                        myItem = clickedMessage.getWantedItem();
                        break;
                }

                Toast.makeText(ChatActivity.this, "you clicked" + clickedMessage.toString(), Toast.LENGTH_LONG).show();
                if(myItem != null && yourItem != null) {

                    BarterTradeConfirmationDialog dialog = new BarterTradeConfirmationDialog(ChatActivity.this, "Trade these items?" ,myItem, yourItem);

                    final ItemData finalMyItem = myItem;
                    final ItemData finalYourItem = yourItem;
                    dialog.setOnOKClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showFinishedTradePopup(finalMyItem,finalYourItem);
                            //finalizeTrade(finalMyItem, finalYourItem, clickedMessage);
                        }
                    });
                    dialog.show();
                }
            }
        }

        ;
    };

    private void deleteOffer() {

    }

    private String messageThreadID;
    private String myID;
    private String yourID;


    private void finalizeTrade(final ItemData finalMyItem, final ItemData finalYourItem, MessageDataModel message) {
        String newBody = you.getFullName() + "'s offer: " + finalYourItem.getTitle() + " for " + finalMyItem.getTitle() + "\n ACCEPTED";
        String currentUserId = DB.myUid();
        Map<String, Object> map = new HashMap<>();
        //use map to update every node at once and call one success listener
        map.put("/users/" + currentUserId + "/tradedWith/" + finalYourItem.getSeller_user_UUID() + "/", true);
        map.put("/users/" + finalYourItem.getSeller_user_UUID() + "/tradedWith/" + currentUserId + "/", true);
        map.put("/messageThreads_messages/" + messageThreadID + "/" + message.key + "/agreed/" , true);//todo instead mark as agreed and display message as agreed body agreed to trade titles
        map.put("/messageThreads_messages/" + messageThreadID + "/" + message.key + "/body/" , newBody);//todo instead mark as agreed and display message as agreed body agreed to trade titles
        map.put("/items/" + finalMyItem.getId() + "/", null );
        map.put("/items_by_user/" + finalMyItem.getId() + "/", null );
map.put("/categories/" + finalMyItem.getCategory() + "/" + finalMyItem.getId() + "/", null);
        //remove them from items,categories not possible sonce there may be
        //some offer messages still referring to it from your messageThreads
        // delete the message
        // set tradedWith of both usersto each other
        DB.root.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showFinishedTradePopup(finalMyItem,finalYourItem);
            }
        });
         //mark both items as agreed or delete them
        /** TODO
         * okay so leave it in db then and what if item is agreed   do not show it?
         * how to request only for non-agreed items?
         *
         * OR
         * delete but when ChatRVAdapter requests non existing item then change the id
         * of item on one unique singleton-ish id containing item that has picture saying sold Title saying sold and so on?
         *
         * Or
         * when
         * when chatRVadapter encounters  agreed item just delete the offer message?
         *
         * wait !! the offer itself has agreed status! if agreed is true don't bother displaying or search item id
         * in agreed_items list
         *
         * if message in message thread is agreed imageviews are not displayed and items not
         * searched
         * or item will be only on agreed list.
         * AlwAys if item datasnapshot.exist() is false or item is null just diplay default item number "default_non_available" with crossed picture
         * stored not in firebase but as static field on ItemData.NOT_AVAILABLE
         */
    }

    private void showFinishedTradePopup(ItemData myItem,ItemData yourItem) {
        BarterTradeConfirmationDialog d = new
                BarterTradeConfirmationDialog(this,
                "You Have Succesfully traded with " + you.getFullName(), myItem , yourItem );
        d.isTradeFinalized(true);
        d.show();

    }

    private Toolbar barterBar;
    private CircleImageView ivLeftmostimgToolbar;
    private CircleImageView ivProfilepicToolbar;
    private ImageView ivInboxIconToolbar;
    private TextView tvUnreadThreadsAmountCircle;
    private TextView tvMainTextToolbar;
    private UserDataModel you;
    private CircleImageView ivSendMessage;
    private List<MessageDataModel> messages;
    private boolean msgThreadExists;
    private LinearLayoutManager linearLayoutManager;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;
    private ChatRVAdapter adapter;
    private EditText etMsgBodyInput;




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
        etMsgBodyInput = findViewById(R.id.et_msgBodyInput_chat);

        ivProfilepicToolbar.setVisibility(View.GONE);
        ivInboxIconToolbar.setVisibility(View.GONE);
        tvUnreadThreadsAmountCircle.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(this);
        mInflater = LayoutInflater.from(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        messages = new ArrayList<>();
        adapter = new ChatRVAdapter(mInflater, messages);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(clickListener);

        //

        Intent intentThatStartedChat = getIntent();
        you = (UserDataModel) intentThatStartedChat.getSerializableExtra("otherUser");
        updateUI();

        myID = DB.me.getUid();
        yourID = you.getUuid();
        // TODO: 19/11/2019 chck if messagethread exists and open it
        // TODO: 17/11/2019 code chat activity

        messageThreadID = DB.calculateMessageThreadID(myID, yourID);

        DB.messageThreads_messages.child(messageThreadID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot messagesDS) {
                msgThreadExists = messagesDS.exists();
                if (msgThreadExists) {
                    Toast.makeText(ChatActivity.this, "thread exists hurray!", Toast.LENGTH_LONG).show();
                    messages.clear();
                    for (DataSnapshot messageDS : messagesDS.getChildren()) {
                        MessageDataModel message = messageDS.getValue(MessageDataModel.class);
                        message.key = messageDS.getKey();
                        messages.add(message);
                    }
                    adapter.notifyDataSetChanged();
                }
                // TODO: 19/11/2019 open thread in chat activity
                else {
                    Toast.makeText(ChatActivity.this, "no thread we have to create one just before sending your 1st message ", Toast.LENGTH_LONG).show();
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
        String body = etMsgBodyInput.getText().toString();
        if (! body.trim().isEmpty()) {
            MessageDataModel lastWrittenMessage = MessageDataModel.makeNormalMessage(yourID,body);
            if (!msgThreadExists){
                MessageThreadDataModel mtdm = new MessageThreadDataModel(body);
                DB.messageThreads.child(messageThreadID).setValue(mtdm);
            }
            DB.messageThreads_messages.child(messageThreadID).push().setValue(lastWrittenMessage);
            etMsgBodyInput.setText("");
        }
    }


    private void onMessagesLoaded() {

    }


    public void updateUI() {
        Picasso.get().load(you.getPicture()).into(ivLeftmostimgToolbar);
        tvMainTextToolbar.setText(you.getFullName());
    }

}
