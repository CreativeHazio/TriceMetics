package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.fragments.BookMakeupFragment;
import com.creativehazio.tricesignature.fragments.CategoryFragment;
import com.creativehazio.tricesignature.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        replaceFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_icon:
                        replaceFragment(new HomeFragment());
                        return true;
                    case R.id.shop_icon:
                        replaceFragment(new CategoryFragment());
                        return true;
                    case R.id.book_makeup_icon:
                        replaceFragment(new BookMakeupFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();
    }

    private void initViews() {
        bottomNav = findViewById(R.id.bottom_nav);
    }
}