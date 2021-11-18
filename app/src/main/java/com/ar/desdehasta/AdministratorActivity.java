package com.ar.desdehasta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class
AdministratorActivity extends AppCompatActivity {
    private LinearLayout circuitos;
    private LinearLayout store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        circuitos=findViewById(R.id.ly_circuitos);
        store=findViewById(R.id.ly_store);


        circuitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AltaCircuitoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AltaStoreActivity.class);
                startActivityForResult(intent, 0);
            }
        });


    }
}