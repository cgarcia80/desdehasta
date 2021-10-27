package com.ar.desdehasta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ar.desdehasta.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/* */
public class MainActivity extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";

    private GoogleSignInClient mSignInClient;

    private ActivityMainBinding mBinding;

    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.signOut.setOnClickListener(v -> signOut());
        // Initialize Firebase Auth and check if the user is signed in
        mFirebaseAuth = FirebaseAuth.getInstance();

        Button circuitos = findViewById(R.id.btnCircuitos);
        circuitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListadoCircuitosActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        Button grupos = findViewById(R.id.btnGrupos);
        grupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AltaPersonasActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        if (mFirebaseAuth.getCurrentUser() == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
       // setProfile();

    }
/*
    private void setProfile() {
        Glide.with(this).load(getUserPhotoUrl()).into(mBinding.profileImage);
        mBinding.profileName.setText(getUserName());
    }*/

    private void signOut() {
        mFirebaseAuth.signOut();
        mSignInClient.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private String getUserPhotoUrl() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }
        return null;
    }

    private String getUserName() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }

        return ANONYMOUS;
    }
}