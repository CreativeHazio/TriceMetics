package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.adapters.EmptyRecyclerView;
import com.creativehazio.tricesignature.adapters.ProductsRecycler;
import com.creativehazio.tricesignature.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductByCategoryActivity extends AppCompatActivity {
    public final static String EXTRA_ID = "productCategoryPosition";

    private FirebaseFirestore firestore;
    private ArrayList<Product> allProductsByCategory;
    private ProductsRecycler recyclerAdapter;
    private View emptyView;
    private TextView categoryTxt;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);
        initViews();

        toolbar.setTitle("LOGO HERE");
        setSupportActionBar(toolbar);

        String productCategory = getIntent().getStringExtra(EXTRA_ID);
        String capitalizedProductCategory = capitalizeFirstLetter(productCategory);

        allProductsByCategory = new ArrayList<>();
        recyclerAdapter = new ProductsRecycler(ProductByCategoryActivity.this,allProductsByCategory);

        firestore.collection("products")
                .whereEqualTo("category",capitalizedProductCategory)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            categoryTxt.setText(productCategory);
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Product product = documentSnapshot.toObject(Product.class);
                                allProductsByCategory.add(product);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        EmptyRecyclerView emptyRecyclerView = findViewById(R.id.product_by_category_recycler);
        emptyRecyclerView.setEmptyView(emptyView);
        emptyRecyclerView.setAdapter(recyclerAdapter);

        emptyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        emptyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        recyclerAdapter.setListener(new ProductsRecycler.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(ProductByCategoryActivity.this,ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_ID,allProductsByCategory.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        firestore = FirebaseFirestore.getInstance();
        emptyView = findViewById(R.id.empty_data_parent);
        categoryTxt = findViewById(R.id.product_by_category_textview);
        toolbar = findViewById(R.id.product_by_category_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_fragment_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.home_fragment_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setImageResource(R.drawable.search_product_icon);

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchProduct(String text) {
        ArrayList<Product> filterProduct = new ArrayList<>();
        for (Product product : allProductsByCategory){
            if (product.getName().toLowerCase().contains(text.toLowerCase())
                    || product.getDescription().toLowerCase().contains(text.toLowerCase())){
                filterProduct.add(product);
            }
        }
        if (filterProduct.isEmpty()){
            Toast.makeText(ProductByCategoryActivity.this, "No Match found", Toast.LENGTH_SHORT).show();
        }else {
            recyclerAdapter.filterList(filterProduct);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_fragment_profile:
                startActivity(new Intent(ProductByCategoryActivity.this,ProfileActivity.class));
                return true;
            case R.id.home_fragment_cart:
                startActivity(new Intent(ProductByCategoryActivity.this, CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String capitalizeFirstLetter(String input) {
        input = input.toLowerCase();

        String[] words = input.split("\\s+");
        StringBuilder sb = new StringBuilder(input.length());
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0))).append(
                    words[0].substring(1, words[0].length()));
            for (int ii = 1; ii < words.length; ii++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[ii].charAt(0)))
                        .append(words[ii].substring(1, words[ii].length()));
            }
        }
        return sb.toString();
    }
}