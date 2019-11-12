package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.InboxElement;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class InboxElementRVAdapter extends RecyclerView.Adapter<InboxElementRVAdapter.InboxElementHolder> implements Filterable, Addable<InboxElement> {
    private OnItemClickListener onItemClickListener;
    //    private Context mContext;
    private List<InboxElement> items;
    private List<InboxElement> itemsFull;
    //private Random rnd;
    private LayoutInflater mInflater;
    private DatabaseReference DB_users;


    public interface OnItemClickListener {
        void onItemClick(InboxElement clickedInboxElement);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public InboxElementRVAdapter(LayoutInflater mInflater, List<InboxElement> items, DatabaseReference db_users) {
        this.DB_users = db_users;
        this.items = items;
        this.itemsFull = new ArrayList<>(items);
        this.mInflater = mInflater;
    }

    public void updateItems(List<InboxElement> newList) {
        this.items = newList;
        this.itemsFull = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void add(InboxElement inboxElement) {
        this.itemsFull.add(inboxElement);
        this.items.add(inboxElement);
        notifyDataSetChanged();

        //now request for User
    }

    @NonNull
    @Override
    public InboxElementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewRow = mInflater
                .inflate(R.layout.inbox_recyclerview_element, parent, false);
        return new InboxElementHolder(viewRow);
    }


    @Override
    public void onBindViewHolder(@NonNull InboxElementHolder holder, int position) {
        InboxElement inboxElement = items.get(position);


        // idea: Load the additional Data Only when it is needed ( when the views are bound)
        // its as well only solution i could come up with to Add Other User pictures and names without repeating all data in firebase just for sake of accommodating the design of the
        // view, and worrying how to update user names in all nodes when user changes his details
        if (inboxElement.isComplete()) { // if all data are present during binding just display them
            holder.progressBar_inboxElement_incompleteYet.setVisibility(View.GONE);
            holder.UserInterlocutor_firstLastName.setText(inboxElement.getUserInterlocutor_firstLastName());
            Picasso.get()
                    .load(inboxElement.getUserInterlocutor_image())
//                .centerCrop()
                    .fit()
                    .centerInside()
                    .into(holder.UserInterlocutor_image);
        } else {
            // if other user image is not present request user object and store his data here (think about storing whole user , when inbox element is clicked then it is easy to
            DB_users.child(inboxElement.getOtherUserID()).addListenerForSingleValueEvent(new OnUserDataModelLoaded(this, inboxElement, position));
            holder.progressBar_inboxElement_incompleteYet.setVisibility(View.VISIBLE);
        }


        holder.lastMessageBody.setText(inboxElement.getLastMessageBody());
        holder.lastMessageTime.setText(inboxElement.getLastMessageTimeAsString());

        if (inboxElement.isRead()) {
            holder.lastMessageBody.setBackgroundColor(0xFFFF0000);//red opaque
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // TODO: 11/11/2019 should be extracted and just simply added to each datamodel that requires some users data for displaying its contents
    /**
     * Event Listener completing the inboxElement with the corresponding user information
     * todo should be extracted and just simply added to each datamodel that requires some users data for displaying its contents
     */
    public class OnUserDataModelLoaded implements ValueEventListener {
        private InboxElement inboxElement;
        private RecyclerView.Adapter parentAdapter;
        private int position;

        public OnUserDataModelLoaded(RecyclerView.Adapter parentAdapter, InboxElement inboxElement, int position) {
            this.inboxElement = inboxElement;
            this.parentAdapter = parentAdapter;
            this.position = position;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserDataModel user = dataSnapshot.getValue(UserDataModel.class); // todo this could be just a field on InboxElement - it would save a lot of hassle and offer other possibilities later (for example is user active and so on)
            inboxElement.setUserInterlocutor_image(user.getPicture());
            inboxElement.setUserInterlocutor_firstLastName(user.getFirstName() + " " + user.getLastName());
            inboxElement.setComplete(true);// now Views should get data from this element
            parentAdapter.notifyItemChanged(position);// tell adapter to re-bind item
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("BOLO", databaseError.getMessage());
        }
    }

    // View holder

    /**
     * View holder
     */
    public class InboxElementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView UserInterlocutor_image;
        private TextView UserInterlocutor_firstLastName;
        private TextView lastMessageBody;
        private TextView lastMessageTime;
        private ProgressBar progressBar_inboxElement_incompleteYet;

        public InboxElementHolder(@NonNull View itemView) {
            super(itemView);
            UserInterlocutor_image = itemView.findViewById(R.id.imageView_inboxElement_UserInterlocutor_image);
            UserInterlocutor_firstLastName = itemView.findViewById(R.id.textView_inboxElement_UserInterlocutor_firstLastName);
            lastMessageBody = itemView.findViewById(R.id.textView_inboxElement_lastMessageBody);
            lastMessageTime = itemView.findViewById(R.id.textView_inboxElement_lastMessageTime);
            progressBar_inboxElement_incompleteYet = itemView.findViewById(R.id.progressBar_inboxElement_incompleteYet);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(items.get(position));
                }
            }
        }
    }


    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     *
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InboxElement> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemsFull);
            } else {
                String searchQuery = constraint.toString().toLowerCase().trim();

                for (InboxElement category : itemsFull) {
                    if (category.getUserInterlocutor_firstLastName().toLowerCase().contains(searchQuery)) {
                        filteredList.add(category);
                    }
                }


            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };


}

