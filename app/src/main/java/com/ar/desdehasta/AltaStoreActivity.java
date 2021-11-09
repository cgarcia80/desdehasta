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
import android.widget.Toast;

import com.ar.desdehasta.pojo.Store;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltaStoreActivity extends AppCompatActivity {

    private List<Store> listStores = new ArrayList<>();
    ArrayAdapter<Store> arrayAdapterStore;

    EditText nomS, dirS,LocS,latS,lgnS,telS,webS;
    ListView listV_Store;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Store storeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        nomS = findViewById(R.id.txt_nombreStore);
        dirS = findViewById(R.id.txt_direccionStore);
        LocS = findViewById(R.id.txt_localidadStore);
        latS = findViewById(R.id.txt_lngStorage);
        lgnS = findViewById(R.id.txt_latStorage);
        telS = findViewById(R.id.txt_telStorage);
        webS = findViewById(R.id.txt_webStore);

        listV_Store = findViewById(R.id.lv_datosStore);
        inicializarFirebase();
        listarDatos();

        listV_Store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                storeSelected = (Store) parent.getItemAtPosition(position);
                nomS.setText(storeSelected.getNombre());
                dirS.setText(storeSelected.getDireccion());
                LocS.setText(storeSelected.getLocalidad());
                latS.setText(storeSelected.getLatitud());
                lgnS.setText(storeSelected.getLongitud());
                telS.setText(storeSelected.getTelefono());
                webS.setText(storeSelected.getSitioweb());
            }
        });

    }

    private void listarDatos() {
        databaseReference.child("Stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listStores.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Store p = objSnaptshot.getValue(Store.class);
                    listStores.add(p);

                    arrayAdapterStore = new ArrayAdapter<Store>(AltaStoreActivity.this, android.R.layout.simple_list_item_1, listStores);
                    listV_Store.setAdapter(arrayAdapterStore);
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

        String nombre = nomS.getText().toString();
        String direccion = dirS.getText().toString();
        String localidad = LocS.getText().toString();
        String latitude = latS.getText().toString();
        String longitude = lgnS.getText().toString();
        String telefono = telS.getText().toString();
        String web = webS.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nombre.equals("")||direccion.equals("")||localidad.equals("")||latitude.equals("")||longitude.equals("")){
                    validacion();
                }
                else {
                    Store s = new Store();
                    s.setUid(UUID.randomUUID().toString());
                    s.setNombre(nombre);
                    s.setDireccion(direccion);
                    s.setLocalidad(localidad);
                    s.setLatitud(latitude);
                    s.setLongitud(longitude);
                    s.setTelefono(telefono);
                    s.setSitioweb(web);
                    databaseReference.child("Stores").child(s.getUid()).setValue(s);
                    //databaseReference.push().setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                if (nombre.equals("")||direccion.equals("")||localidad.equals("")||latitude.equals("")||longitude.equals("")){
                    validacion();
                }
                else {
                Store s = new Store();
                s.setUid(UUID.randomUUID().toString());
                s.setNombre(nomS.getText().toString().trim());
                s.setDireccion(dirS.getText().toString().trim());
                s.setLocalidad(LocS.getText().toString().trim());
                s.setLatitud(latS.getText().toString().trim());
                s.setLongitud(lgnS.getText().toString().trim());
                s.setTelefono(telS.getText().toString().trim());
                s.setSitioweb(webS.getText().toString().trim());
                databaseReference.child("Stores").child(s.getUid()).setValue(s);
                Toast.makeText(this,"Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                }
                break;
            }
            case R.id.icon_delete:{
                Store s = new Store();
                s.setUid(storeSelected.getUid());
                databaseReference.child("Stores").child(s.getUid()).removeValue();
                Toast.makeText(this,"Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nomS.setText("");
        dirS.setText("");
        LocS.setText("");
        latS.setText("");
        lgnS.setText("");
        telS.setText("");
        webS.setText("");
    }
    private void validacion() {
        String nombre = nomS.getText().toString();
        String direccion = dirS.getText().toString();
        String localidad = LocS.getText().toString();
        String latitude = latS.getText().toString();
        String longitude = lgnS.getText().toString();
        if (nombre.equals("")){
            nomS.setError("Required");
        }
        else if (direccion.equals("")){
            dirS.setError("Required");
        }
        else if (localidad.equals("")){
            LocS.setError("Required");
        }
        else if (latitude.equals("")){
            latS.setError("Required");
        }
        else if (longitude.equals("")){
            lgnS.setError("Required");
        }
    }
}