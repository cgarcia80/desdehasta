package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ar.desdehasta.pojo.Grupo;
import com.ar.desdehasta.pojo.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltaGrupoActivity extends AppCompatActivity {
    private List<Grupo> listGrupo = new ArrayList<>();
    ArrayAdapter<Grupo> arrayAdapterGrupo;

    EditText nombreGrupo, circuito;
    ListView listV_Grupos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Grupo grupoSelected;

    private Spinner spinner;
    private ArrayList<String> arrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_grupo);

        nombreGrupo = findViewById(R.id.txt_nombreGrupo);
        circuito = findViewById(R.id.txt_Circuitos);
        spinner= findViewById(R.id.spinner);

        listV_Grupos = findViewById(R.id.lv_datosgrupos);
        inicializarFirebase();
        listarDatos();
        showDataSpinner();

        listV_Grupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                grupoSelected = (Grupo) parent.getItemAtPosition(position);
                nombreGrupo.setText(grupoSelected.getNombre());
                circuito.setText(grupoSelected.getCircuito());

            }
        });

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
                    String uid=item.child("uid").getValue(String.class);
                    //arrayList.add(nombre + " --> Cant Kilometros: "+(String.valueOf(kilometros))+ " |"+ uid );
                    arrayList.add(nombre + "|"+ uid );

                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<>(AltaGrupoActivity.this,R.layout.style_spinner,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(arrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getSelectedItem().toString();
                        String s =item;
                        String s2 = s.substring(s.indexOf("|")+1);
                        circuito.setText(s2);

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

    private void listarDatos() {
        databaseReference.child("Grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGrupo.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Grupo g = objSnaptshot.getValue(Grupo.class);
                    listGrupo.add(g);

                    arrayAdapterGrupo = new ArrayAdapter<Grupo>(AltaGrupoActivity.this, android.R.layout.simple_list_item_1, listGrupo);
                    listV_Grupos.setAdapter(arrayAdapterGrupo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grupos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nom = nombreGrupo.getText().toString();
        String cir = circuito.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nom.equals("")||cir.equals("")){
                    validacion();
                }
                else {
                    Grupo g = new Grupo();
                    g.setUid(UUID.randomUUID().toString());
                    g.setNombre(nom);
                    g.setCircuito(cir);

                    databaseReference.child("Grupos").child(g.getUid()).setValue(g);
                    //databaseReference.push().setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                if (nom.equals("")||cir.equals("")){
                    validacion();
                }
                else {
                    Grupo g = new Grupo();
                    g.setUid(grupoSelected.getUid());
                    g.setNombre(nombreGrupo.getText().toString().trim());
                    g.setCircuito(circuito.getText().toString().trim());

                    databaseReference.child("Grupos").child(g.getUid()).setValue(g);
                    Toast.makeText(this,"Actualizado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_delete:{
                Grupo g = new Grupo();
                g.setUid(grupoSelected.getUid());
                databaseReference.child("Grupos").child(g.getUid()).removeValue();
                Toast.makeText(this,"Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nombreGrupo.setText("");
        circuito.setText("");

    }

    private void validacion() {
        String nom = nombreGrupo.getText().toString();
        String cir = circuito.getText().toString();

        if (nom.equals("")){
            nombreGrupo.setError("Required");
        }
        else if (cir.equals("")){
            circuito.setError("Required");
        }

    }
}