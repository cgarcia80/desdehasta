package com.ar.desdehasta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.desdehasta.pojo.ListElement;

public class InteresActivityDetail extends AppCompatActivity {

    TextView tvNombre, tvDireccion, tvBarrio, tvCall, tvWeb;
    ImageButton maps,call, web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interes_activity_detail);

        ListElement element = (ListElement) getIntent().getSerializableExtra("ListElement");
        tvNombre = findViewById(R.id.tvNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvBarrio = findViewById(R.id.tvBarrio);
        tvCall = findViewById(R.id.tv_call);
        tvWeb = findViewById(R.id.tv_web);

        tvNombre.setText(element.getNombre());
        tvDireccion.setText("Direccion: "+element.getDireccion());
        tvBarrio.setText("Barrio: "+element.getBarrio());
        if (element.getTelefono().equalsIgnoreCase("null"))
            tvCall.setText("Tel: No posee numero telefonico");
        else
            tvCall.setText("Tel:"+ element.getTelefono());
        //tvGeo1.setText(element.getGeo1());
        //tvGeo2.setText(element.getGeo2());

        maps = findViewById(R.id.btn_img_maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "geo:"+element.getGeo2()+","+element.getGeo1()+"?q=bicicleterias";
                intentMap(location);
            }
        });

        call = findViewById(R.id.btn_img_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri numUri = Uri.parse("tel:" + element.getTelefono());
                Intent i = new Intent(Intent.ACTION_DIAL,numUri); // queda con el numero antes de apretar llamar
                //Intent i = new Intent(Intent.ACTION_CALL,numUri); //Llama directo

                if(element.getTelefono().equalsIgnoreCase("null")){
                    Toast.makeText(getApplicationContext(), element.getNombre() + "No Tiene Telefono Registrado", Toast.LENGTH_SHORT).show();
                }
                else if (i.resolveActivity(getPackageManager()) != null){
                    startActivity(i);
                }
            }
        });

        web = findViewById(R.id.btn_img_web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = element.getWeb();
                Uri webapage = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW,webapage);

                if(element.getWeb().equalsIgnoreCase("null")){
                    Toast.makeText(getApplicationContext(), element.getNombre()+"No Posee Sitio Web", Toast.LENGTH_SHORT).show();
                }
                else if (i.resolveActivity(getPackageManager()) != null){
                    startActivity(i);
                }
            }
        });
    }

    private void intentMap(String location) {
        // Parse the URI and create the intent.
        Uri webpage = Uri.parse(location);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("IntentosImplicitosMapa", "¡No tengo como manejar este Intent!");
        }
    }
}