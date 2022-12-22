package com.creativehazio.tricesignature.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.activities.CartActivity;
import com.creativehazio.tricesignature.activities.ProductByCategoryActivity;
import com.creativehazio.tricesignature.activities.ProfileActivity;
import com.creativehazio.tricesignature.adapters.ProductsCategoryRecycler;
import com.creativehazio.tricesignature.model.ProductCategory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CategoryFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private ProductsCategoryRecycler recyclerAdapter;
    private ArrayList<ProductCategory> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = rootView.findViewById(R.id.category_fragment_toolbar);
        toolbar.setTitle("LOGO HERE");
        activity.setSupportActionBar(toolbar);


        categoryList = new ArrayList<>();
        categoryList.add(new ProductCategory("LASHES",R.drawable.lashes_category));
        categoryList.add(new ProductCategory("EYE PENCILS",R.drawable.eyepencils_category));
        categoryList.add(new ProductCategory("GEL",R.drawable.gel_category));
        categoryList.add(new ProductCategory("FOUNDATION",R.drawable.foundation_category));
        categoryList.add(new ProductCategory("ACCESSORIES",R.drawable.accessories_category));
        categoryList.add(new ProductCategory("SHIMZ",R.drawable.shimz_category));
        categoryList.add(new ProductCategory("LIPS",R.drawable.lips_category));

        recyclerAdapter = new ProductsCategoryRecycler(getActivity(),categoryList);

        categoryRecyclerView = rootView.findViewById(R.id.category_fragment_recycler_view);
        categoryRecyclerView.setAdapter(recyclerAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        recyclerAdapter.setListener(new ProductsCategoryRecycler.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), ProductByCategoryActivity.class);
                intent.putExtra(ProductByCategoryActivity.EXTRA_ID,categoryList.get(position).getCategory());
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.category_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.category_fragment_cart:
                startActivity(new Intent(getActivity(), CartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
                return true;
            case R.id.category_fragment_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}