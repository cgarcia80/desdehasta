package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.desdehasta.pojo.Circuito;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class IniciarCircuitoActivity extends AppCompatActivity  {
    private FusedLocationProviderClient ubicacion;


    private FloatingActionButton navigation_btn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private Spinner spinner;
    private TextView ver;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Double lat_origen;
    private Double lng_origen;
    private Double lat_dest;
    private Double lng_dest;
    private String lat_actual;
    private String lng_actual;
    String circuito;
    String dirOrigen;
    String dirDestino;

    String valor = "-Seleccione su circuito-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_circuito);

        spinner = findViewById(R.id.spinnerNavegacion);
        ver = findViewById(R.id.textViewSeleccionCircutio);
        inicializarFirebase();
        valor = getIntent().getStringExtra("circuito");
        showDataSpinner();

        navigation_btn = findViewById(R.id.navegar);


        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(IniciarCircuitoActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this,"tenemos permisos",Toast.LENGTH_SHORT).show();

                }else{
                    ActivityCompat.requestPermissions(IniciarCircuitoActivity.this,new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                ubicacion= LocationServices.getFusedLocationProviderClient(IniciarCircuitoActivity.this);
                ubicacion.getLastLocation().addOnSuccessListener(IniciarCircuitoActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location !=null){
                            lat_actual=String.valueOf(location.getLatitude());
                            lng_actual=String.valueOf(location.getLongitude());
                            //Toast.makeText(IniciarCircuitoActivity.this, "Latitud"+latitud+"Longitud"+longitud, Toast.LENGTH_SHORT).show();

                            String actualcaracter = lat_actual.toString().substring(0, 6);
                            String origencaracter = lat_origen.toString().substring(0, 6);

                        Log.i("ver",actualcaracter+" "+origencaracter);
                    if(!circuito.contains(getString(R.string.spinnerDefaultDircuito))) {

                        if(actualcaracter.equals(origencaracter) ){

                        //String url="http://maps.google.com/maps?hl=en&saddr=" + String.valueOf(lat_origen) +"," + String.valueOf(lng_origen) +"&daddr=" + String.valueOf(lat_dest)+"," +String.valueOf(lng_dest)+"&mode=b";
                        //String url="http://www.google.com/maps/dir/"+ lat_origen +"," + lng_origen+"/" + lat_dest+"," +lng_dest;
                        //String url="https://www.google.com/maps/dir/?api=1&origin="+ lat_origen + "%2C" + lng_origen + "&destination=" + lat_dest + "%2C" +lng_dest + "&travelmode=biclycling";
                        String url="https://www.google.com/maps/dir/?api=1&origin="+dirOrigen+"&destination="+dirDestino+"&mode=b";
                        Uri gmmIntentUri = Uri.parse(url);

                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat_dest + "," + lng_dest + "&mode=b");

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                    else{
                        Toast.makeText(IniciarCircuitoActivity.this, "Debe estar en la ubicacion inicial del circuito!", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(IniciarCircuitoActivity.this, "Debe seleccionar un circuito!", Toast.LENGTH_SHORT).show();

                }
                        }
                    }
                });
            }

        });
    }

    private void dameUbicacion() {
        if(ContextCompat.checkSelfPermission(IniciarCircuitoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this,"tenemos permisos",Toast.LENGTH_SHORT).show();

        }else{
            ActivityCompat.requestPermissions(IniciarCircuitoActivity.this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        ubicacion= LocationServices.getFusedLocationProviderClient(IniciarCircuitoActivity.this);
        ubicacion.getLastLocation().addOnSuccessListener(IniciarCircuitoActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    lat_actual=String.valueOf(location.getLatitude());
                    lng_actual=String.valueOf(location.getLongitude());
                    //Toast.makeText(IniciarCircuitoActivity.this, "Latitud"+latitud+"Longitud"+longitud, Toast.LENGTH_SHORT).show();

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
            public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add(getString(R.string.spinnerDefaultDircuito));
                for (DataSnapshot item: snapshot.getChildren()){
                    String nombre=item.child("nombre").getValue(String.class);
                     arrayList.add(nombre);

                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<>(IniciarCircuitoActivity.this,R.layout.style_spinner,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(arrayAdapter);

                spinner.setSelection(arrayAdapter.getPosition(valor));

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getSelectedItem().toString();
                        String s =item;
                        circuito = s;

                       // ver.setText(s);

                        DatabaseReference n1= firebaseDatabase.getReference();
                        Query q1 =n1.child("Circuito").orderByChild("nombre").equalTo(item);


                        q1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot chidsSnapshot: snapshot.getChildren()){
                                    Circuito circuito = chidsSnapshot.getValue(Circuito.class);
                                    //String catekey= chidsSnapshot.getKey();
                                    /*lat_origen =String.valueOf(circuito.getLatitude_ori());
                                    lng_origen =String.valueOf(circuito.getLongitude_des());
                                    lat_dest =String.valueOf(circuito.getLatitude_des());
                                    lng_dest =String.valueOf(circuito.getLongitude_des());
                                    */
                                    lat_origen =circuito.getLatitude_ori();
                                    lng_origen =circuito.getLongitude_des();
                                    lat_dest =circuito.getLatitude_des();
                                    lng_dest =circuito.getLongitude_des();
                                    dirOrigen=circuito.getDirOrigen();
                                    dirDestino=circuito.getDirDestino();


                                    ver.setText(circuito.getNombre());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                            }
                        });
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