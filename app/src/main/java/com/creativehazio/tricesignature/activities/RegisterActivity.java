package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativehazio.tricesignature.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private final static int GOOGLE_REQ_CODE = 111;

    private EditText emailEdt;
    private EditText passwordEdt;
    private Button emailLoginBtn;
    private Button googleSignInBtn;
    private Button facebookSignInBtn;
    private Button newUserSignUpBtn;

    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        emailLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    signInWithEmail(email,password);
                }
            }
        });

        newUserSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpActivity();
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void goToSignUpActivity() {
        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
        finish();
    }

    private void signInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, getString(R.string.account_not_found), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), GOOGLE_REQ_CODE,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {}
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GOOGLE_REQ_CODE:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();

                    signInWIthGoogleCredential(idToken);

                } catch (ApiException e) {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void signInWIthGoogleCredential(String idToken){

        if (idToken != null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
                                goToHomeActivity();
                            } else {
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void goToHomeActivity() {
        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
        finish();
    }


    private void initViews() {
        emailEdt = findViewById(R.id.buyer_login_email);
        passwordEdt = findViewById(R.id.buyer_login_password);
        emailLoginBtn = findViewById(R.id.email_log_in_btn);
        googleSignInBtn = findViewById(R.id.google_log_in_btn);
        facebookSignInBtn = findViewById(R.id.facebook_log_in_btn);
        newUserSignUpBtn = findViewById(R.id.sign_up_btn);

        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(getApplicationContext());
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        
    }
}