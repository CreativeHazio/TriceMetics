package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.model.Buyer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdt;
    private EditText usernameEdt;
    private EditText passwordEdt;
    private Button signUpBtn;

    private String email;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = usernameEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                String password = passwordEdt.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    signUp(email,password,name);
                }
            }
        });

    }

    private void signUp(String email, String password,String name) {
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
                        addUserToFirestore(email,password,name);
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, getString(R.string.signed_in_failed), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                        finish();
                    }
                });
    }

    private void addUserToFirestore(String email, String password, String name) {

        firestore.collection("buyers")
                .add(new Buyer(name,email,password))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            System.out.println("Admin Added to firestore successful");
                        }else{
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        cancelSignUp();
    }

    private void cancelSignUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(getString(R.string.cancel_sign_up));
        builder.setCancelable(false);

        builder.setPositiveButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            finish();
        });

        builder.setNegativeButton("Continue sign up", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initViews() {
        emailEdt = findViewById(R.id.buyer_sign_up_email);
        usernameEdt = findViewById(R.id.buyer_sign_up_name);
        passwordEdt = findViewById(R.id.buyer_sign_up_password);
        signUpBtn = findViewById(R.id.new_buyer_sign_up_btn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
    }
}