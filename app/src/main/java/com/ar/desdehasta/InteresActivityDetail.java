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

    TextView tvNombre, tvDireccion, tvBarrio, tvCall;
    ImageButton maps,call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interes_activity_detail);

        ListElement element = (ListElement) getIntent().getSerializableExtra("ListElement");
        tvNombre = findViewById(R.id.tvNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvBarrio = findViewById(R.id.tvBarrio);
        tvCall = findViewById(R.id.tv_call);

        tvNombre.setText(element.getNombre());
        tvDireccion.setText("Direccion: "+element.getDireccion());
        tvBarrio.setText("Barrio: "+element.getBarrio());
        tvCall.setText("Tel:"+ element.getTelefono());
        //tvGeo1.setText(element.getGeo1());
        //tvGeo2.setText(element.getGeo2());

        maps = findViewById(R.id.btn_img_maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "geo:"+element.getGeo1().toString()+","+element.getGeo2().toString();
                intentMap(location);
            }
        });

        call = findViewById(R.id.btn_img_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri numUri = Uri.parse("tel:" + element.getTelefono().toString());
                Intent i = new Intent(Intent.ACTION_DIAL,numUri); // queda con el numero antes de apretar llamar
                //Intent i = new Intent(Intent.ACTION_CALL,numUri); //Llama directo

                if (i.resolveActivity(getPackageManager()) != null){
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