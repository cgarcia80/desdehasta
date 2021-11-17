package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ar.desdehasta.adapter.ListAdapter;
import com.ar.desdehasta.pojo.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteresActivity extends AppCompatActivity {
    DatabaseReference ref;
    ListAdapter listAdapter;
    List<Store> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interes_activity);
        init();
    }

    public void init(){

        elements = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Stores");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Store store = snapshot.getValue(Store.class);
                        elements.add(store);

                    }
                    listAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Store item) {
                moveToDescription(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);


    }

    public void moveToDescription(Store item) {
        Intent intent = new Intent(this, InteresActivityDetail.class);
        intent.putExtra("ListElement", item);
        startActivity(intent);
    }

    private String color(){
        String[] arcoIris = new String[]{"#800080","#000000","#FF0000","#0000FF","#00FF00","#FFFF00","#FF00FF","#00FFFF","#8800FF","#FF8800"};
        Random random = new Random();
        int color = random.nextInt(10);

        return arcoIris[color];
    }
}