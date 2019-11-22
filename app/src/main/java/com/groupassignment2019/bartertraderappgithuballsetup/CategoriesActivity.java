package com.groupassignment2019.bartertraderappgithuballsetup;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groupassignment2019.bartertraderappgithuballsetup.adapters.CategoryRVAdapter;
import com.groupassignment2019.bartertraderappgithuballsetup.models.Category;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    private List<Category> categories;
    private LayoutInflater mInflater;
    private CategoryRVAdapter adapter;


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you consume items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_toolbar_menu, menu);
        MenuItem searchCategoryItem = menu.findItem(R.id.action_search);
        SearchView searchView = (androidx.appcompat.widget.SearchView) searchCategoryItem.getActionView();

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
        setContentView(R.layout.activity_categories);
        setTitle("Choose one of Categories");
        categories = prepareCategoriesList();
        prepareRecyclerView(categories);

        TextView tv = findViewById(R.id.ScreenTitle);
    }

    private List prepareCategoriesList() {
        return Category.getAllCategories();
    }

    private void prepareRecyclerView(List<Category> categories) {
        mInflater = LayoutInflater.from(this.getBaseContext());
        RecyclerView recyclerView = findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        adapter = new CategoryRVAdapter(mInflater, categories);


        CategoryRVAdapter.OnItemClickListener clickListener = new CategoryRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category clickedCategory) {
                Toast.makeText(CategoriesActivity.this, "you clicked" + clickedCategory.getCategoryTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CategoriesActivity.this, ItemsListActivity.class);
                intent.putExtra("by","category");
                intent.putExtra("category",clickedCategory.getCategoryTitle());//sends the name of the selected category to ItemsByAnythingActivity
                startActivity(intent);
            }
        };

        adapter.setOnItemClickListener(clickListener);

        recyclerView.setAdapter(adapter);
    }


}
