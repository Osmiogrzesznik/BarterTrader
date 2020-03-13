package com.groupassignment2019.bartertraderappgithuballsetup.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.groupassignment2019.bartertraderappgithuballsetup.R;
import com.groupassignment2019.bartertraderappgithuballsetup.models.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder> implements Filterable {
    private OnItemClickListener onItemClickListener;
    private List<Category> categories;
    private List<Category> categoriesFull;
    //private Random rnd;
    private LayoutInflater mInflater;

    public interface OnItemClickListener {
        void onItemClick(Category clickedCategory);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryRVAdapter(LayoutInflater mInflater, List<Category> categories) {

        this.categories = categories;
        this.categoriesFull = new ArrayList<>(categories);
        this.mInflater = mInflater;
        //this.rnd = new Random();

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View cardview = mInflater
                .inflate(R.layout.cardview_category, parent, false);
        return new CategoryViewHolder(cardview);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.textViewCategoryTitle.setText(category.getCategoryTitle());
        holder.imageViewCategoryCard.setImageResource(category.getImageResource());
        //holder.categoryCardParentWrapper.setBackgroundColor(Color.argb(200, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
    holder.categoryCardParentWrapper.setBackgroundResource(category.backgroundDrawableId);
    holder.textViewCategoryDescription.setText(category.description);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    // View holder

    /**
     * View holder
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AppCompatImageView imageViewCategoryCard;
        private TextView textViewCategoryTitle;
        private TextView textViewCategoryDescription;
        private ConstraintLayout categoryCardParentWrapper;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCardParentWrapper = itemView.findViewById(R.id.categoryCardParentWrapper);
            textViewCategoryTitle = itemView.findViewById(R.id.textViewCategoryTitle);
            imageViewCategoryCard = itemView.findViewById(R.id.item_cardview_imageView);
            textViewCategoryDescription = itemView.findViewById(R.id.textViewCategoryDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(categories.get(position));
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
            List<Category> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(categoriesFull);
            } else {
                String searchQuery = constraint.toString().toLowerCase().trim();

                for (Category category : categoriesFull) {
                    if (category.getCategoryTitle().toLowerCase().contains(searchQuery)) {
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
            categories.clear();
            categories.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };



}

