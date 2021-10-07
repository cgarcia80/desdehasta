package com.ar.desdehasta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.desdehasta.adapter.AdapterCircuito;
import com.ar.desdehasta.pojo.Circuito;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Circuito> list;
    RecyclerView rv;
    SearchView searchView;
    AdapterCircuito adapter;
    LinearLayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button info = findViewById(R.id.cargarmapa);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), AltaCircuitoActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        ref=FirebaseDatabase.getInstance().getReference().child("Circuito");
        rv= findViewById(R.id.rv);
        searchView=findViewById(R.id.search);
        lm=new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list= new ArrayList<>();
        adapter= new AdapterCircuito(list);
        rv.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Circuito circuito = snapshot.getValue(Circuito.class);
                        list.add(circuito);

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }
}