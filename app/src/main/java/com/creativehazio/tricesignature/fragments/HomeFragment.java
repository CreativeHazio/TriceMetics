package com.creativehazio.tricesignature.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.activities.CartActivity;
import com.creativehazio.tricesignature.activities.ProductDetailActivity;
import com.creativehazio.tricesignature.activities.ProfileActivity;
import com.creativehazio.tricesignature.adapters.ProductsRecycler;
import com.creativehazio.tricesignature.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private SearchView searchView;
    private ArrayList<Product> allProducts;
    private ProductsRecycler recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Setup toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = rootView.findViewById(R.id.home_fragment_toolbar);
        toolbar.setTitle("LOGO HERE");
        activity.setSupportActionBar(toolbar);

        //Setup recyclerview
        allProducts = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.home_fragment_recycler_view);
        recyclerAdapter = new ProductsRecycler(getActivity(),allProducts);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        /**Get all products from the #collection in Trice database
         * and set it to the arraylist that will be added to my recyclerview */
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("products")
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                Product product = documentSnapshot.toObject(Product.class);
                                                allProducts.add(product);
                                            }
                                            recyclerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });

        recyclerView.setAdapter(recyclerAdapter);

        //Setup onClickListener
        recyclerAdapter.setListener(new ProductsRecycler.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_ID,allProducts.get(position).getId());
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_fragment_profile:
                startActivity(new Intent(getActivity(),ProfileActivity.class));
                return true;
            case R.id.home_fragment_cart:
                startActivity(new Intent(getActivity(), CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchProduct(String text) {
        ArrayList<Product> filterProduct = new ArrayList<>();
        for (Product product : allProducts){
            if (product.getName().toLowerCase().contains(text.toLowerCase())
                    || product.getDescription().toLowerCase().contains(text.toLowerCase())){
                filterProduct.add(product);
            }
        }
        if (filterProduct.isEmpty()){
            Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_SHORT).show();
        }else {
            recyclerAdapter.filterList(filterProduct);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.home_fragment_menu, menu);
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}