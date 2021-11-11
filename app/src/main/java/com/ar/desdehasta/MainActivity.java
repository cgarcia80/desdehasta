package com.ar.desdehasta;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{
    public static final String ANONYMOUS = "anonymous";
    private GoogleSignInClient mSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private LinearLayout circuitos;
    private LinearLayout grupos;
    private LinearLayout agenda;
    private LinearLayout interes;
    private LinearLayout salir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inicio");
        circuitos=findViewById(R.id.layout1);
        grupos=findViewById(R.id.layout2);
        agenda=findViewById(R.id.layout3);
        interes=findViewById(R.id.layout4);
        //salir=findViewById(R.id.layout5);

        circuitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListadoCircuitosActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        grupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IniciarCircuitoActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        interes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InteresActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgendaDeEventosActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //salir.setOnClickListener(v -> signOut());
        // Initialize Firebase Auth and check if the user is signed in
        mFirebaseAuth = FirebaseAuth.getInstance();



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

        getUserFirebase();
    }

    private void signOut() {
        mFirebaseAuth.signOut();
        mSignInClient.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private String getUserFirebase() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            if(user.getUid().equals("s0T9trdzZMZhRPAZcBCSQ9o3SCp2")) {
                Toast.makeText(this, "usuarioo..ADMIN....." + user.getDisplayName() + "- id --" + user.getUid(), Toast.LENGTH_SHORT).show();
                Button resetButton=(Button)findViewById(R.id.admin); resetButton.setVisibility(View.VISIBLE);
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AdministratorActivity.class);
                        startActivityForResult(intent, 0);
                    }
                });
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idItem = item.getItemId();

        switch (idItem){
            case R.id.logout:
                Toast.makeText(this, "Cerrado", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}