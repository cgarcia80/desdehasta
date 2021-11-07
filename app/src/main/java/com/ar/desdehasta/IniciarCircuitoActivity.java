package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.ar.desdehasta.pojo.Grupo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IniciarCircuitoActivity extends AppCompatActivity {
    private FloatingActionButton navigation_btn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private Spinner spinner;
    private ArrayList<String> arrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_circuito);

        spinner= findViewById(R.id.spinnerNavegacion);

        inicializarFirebase();
        showDataSpinner();

        navigation_btn=findViewById(R.id.navegar);


        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q=-34.575506,-58.4405749&mode=b");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                if(mapIntent.resolveActivity(getPackageManager())!=null){
                    startActivity(mapIntent);
                }
            }
        });
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    private void showDataSpinner() {
        databaseReference.child("Circuito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add("_");
                for (DataSnapshot item: snapshot.getChildren()){
                    String nombre=item.child("nombre").getValue(String.class);
                    // int kilometros=item.child("kilometros").getValue(Integer.class);
                    //String uid=item.child("uid").getValue(String.class);
                    //arrayList.add(nombre + " --> Cant Kilometros: "+(String.valueOf(kilometros))+ " |"+ uid );
                    //arrayList.add(nombre + "|"+ uid );
                    arrayList.add(nombre);
                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<>(IniciarCircuitoActivity.this,R.layout.style_spinner,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(arrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getSelectedItem().toString();
                        String s =item;


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}