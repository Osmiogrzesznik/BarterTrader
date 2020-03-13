package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemDataViewHolder> implements Filterable{
    private OnItemClickListener onItemClickListener;
//    private Context mContext;
    private List<ItemData> items;
    private List<ItemData> itemsFull;
    //private Random rnd;
    private LayoutInflater mInflater;
    private boolean isViewerTheOwner;

    public interface OnItemClickListener {
        void onItemClick(ItemData clickedItemData);
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ItemRVAdapter( LayoutInflater mInflater, List<ItemData> items) {
//this.mContext = context;
        this.items = items;
        this.itemsFull = new ArrayList<>(items);
        this.mInflater = mInflater;
        //this.rnd = new Random();
        this.isViewerTheOwner = false;
    }


    public void addOrModifyItemData(String key, ItemData itemData){
        this.itemsFull.add(itemData);
        this.items.add(itemData);
        notifyDataSetChanged();
    }

    public void removeItemData(String key) {
        String msg = "some item is missing in the list ";
        Log.d("BOLO", msg);
       // Toast.makeText(, "", Toast.LENGTH_SHORT).show();
    }



    public void setIsViewerTheOwner(boolean b){
        this.isViewerTheOwner = b;
    }


    @NonNull
    @Override
    public ItemDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View cardview = mInflater
                .inflate(R.layout.cardview_item_in_item_list, parent, false);
        return new ItemDataViewHolder(cardview, isViewerTheOwner);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemDataViewHolder holder, int position) {
        ItemData item = items.get(position);
holder.textViewItemDataTitle.setText(item.getTitle());
if (isViewerTheOwner){
holder.Button_EditOwnedItem_In_ItemListElementCardView.setVisibility(View.VISIBLE);
}
// TODO: 01/11/2019 should each category be colorcoded - so lists with toys have bright color, and those with tools dark
        Picasso.get()
                .load(item.getPictureURI())
//                .centerCrop()
                .fit()
                .centerInside()
                .into(holder.imageViewItemDataCard);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // View holder

    /**
     * View holder
     */
    public class ItemDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AppCompatImageView imageViewItemDataCard;
        private TextView textViewItemDataTitle;
        private Button Button_EditOwnedItem_In_ItemListElementCardView;

        ItemDataViewHolder(@NonNull View itemView, boolean isOwner) {
            super(itemView);
            ConstraintLayout categoryCardParentWrapper = itemView.findViewById(R.id.item_CardParentWrapper);
            textViewItemDataTitle = itemView.findViewById(R.id.textViewItemTitle);
            imageViewItemDataCard = itemView.findViewById(R.id.item_cardview_imageView);
            Button_EditOwnedItem_In_ItemListElementCardView = itemView.findViewById(R.id.Button_EditOwnedItem_In_ItemListElementCardView);

            if(isOwner){

                Button_EditOwnedItem_In_ItemListElementCardView.setVisibility(View.VISIBLE);

            }
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
            List<ItemData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemsFull);
            } else {
                String searchQuery = constraint.toString().toLowerCase().trim();

                for (ItemData category : itemsFull) {
                    if (category.getTitle().toLowerCase().contains(searchQuery)) {
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

