package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.model.Buyer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    private Switch darkModeSwitch;
    private boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ShapeableImageView profileImage;
    private TextView profileName;
    private Button signOutButton;

    private String profileNameFromFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();

        if (nightMode){
            darkModeSwitch.setChecked(true);
        }

        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });

        Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(profileImage);
        getProfileName();

//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                startActivity(new Intent(ProfileActivity.this, RegisterActivity.class));
//                finish();
//            }
//        });
    }

    private void getProfileName() {
        firestore.collection("buyers")
                .whereEqualTo("email",currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Buyer buyer = documentSnapshot.toObject(Buyer.class);
                                profileNameFromFirestore = buyer.getName();
                            }
                        }
                    }
                });

        profileName.setText(profileNameFromFirestore);
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night",false);

        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
//        signOutButton = findViewById(R.id.sign_out_of_app_btn);
    }

}